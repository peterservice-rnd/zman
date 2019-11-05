package com.peterservice.zman.controllers

import com.peterservice.zman.api.entities.CreationConflicts
import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.api.exceptions.AliasNotFoundException
import com.peterservice.zman.api.repositories.AliasRepository
import com.peterservice.zman.api.zookeeper.ZookeeperService
import com.peterservice.zman.api.zookeeper.ZookeeperServiceManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.security.Principal

import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/zk/{alias}/**")
class ZNodeController {

    @Autowired
    private lateinit var aliasRepository: AliasRepository

    @Autowired
    private lateinit var zookeeperServiceManager: ZookeeperServiceManager

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getZNode(@PathVariable alias: String,
                 @RequestAttribute zpath: String,
                 @RequestParam(defaultValue = "false") export: Boolean,
                 @AuthenticationPrincipal principal: Principal?,
                 response: HttpServletResponse): ZNode {
        val zookeeperService = getService(alias)

        if (export) {
            addAttachmentHeader(response, buildFilename(alias, zpath))
            return zookeeperService.readZNode(zpath, recursive = true, user = principal?.name)
        }

        return zookeeperService.readZNode(zpath, recursive = false, user = principal?.name)
    }

    private fun buildFilename(alias: String, zpath: String): String {
        return zpath.substringAfterLast('/').takeIf(String::isNotBlank) ?: alias
    }

    private fun addAttachmentHeader(response: HttpServletResponse, filename: String) {
        response.addHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=$filename"
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createZNode(@PathVariable alias: String,
                    @RequestAttribute zpath: String,
                    @RequestParam(defaultValue = "false") overwrite: Boolean,
                    @RequestBody znode: ZNode,
                    @AuthenticationPrincipal principal: Principal?): CreationConflicts {
        val zookeeperService = getService(alias)
        val conflicts = zookeeperService.createZNode(zpath, znode, overwrite, principal?.name)
        return CreationConflicts(conflicts)
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun updateZNode(@PathVariable alias: String,
                    @RequestAttribute zpath: String,
                    @RequestBody znode: ZNode,
                    @AuthenticationPrincipal principal: Principal?) {
        val zookeeperService = getService(alias)
        zookeeperService.updateZNode(zpath, znode, principal?.name)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun deleteZNode(@PathVariable alias: String,
                    @RequestAttribute zpath: String,
                    @AuthenticationPrincipal principal: Principal?) {
        val zookeeperService = getService(alias)
        zookeeperService.deleteZNode(zpath, principal?.name)
    }

    private fun getService(alias: String): ZookeeperService {
        val server = aliasRepository.findByAlias(alias) ?: throw AliasNotFoundException("Alias '$alias' was not found")
        return zookeeperServiceManager.getServiceFor(server)
    }
}
