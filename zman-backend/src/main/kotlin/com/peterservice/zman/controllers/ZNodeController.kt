package com.peterservice.zman.controllers

import com.peterservice.zman.api.entities.CreationConflicts
import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.api.exceptions.AliasNotFoundException
import com.peterservice.zman.api.repositories.AliasRepository
import com.peterservice.zman.api.zookeeper.ZookeeperService
import com.peterservice.zman.api.zookeeper.ZookeeperServiceManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

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
                 @RequestParam(defaultValue = "json") format: String,
                 response: HttpServletResponse): ZNode {
        val zookeeperService = getService(alias)

        if (export) {
            addAttachmentHeader(response, buildFilename(alias, zpath, format))
            return zookeeperService.readZNode(zpath, recursive = true)
        }

        return zookeeperService.readZNode(zpath, recursive = false)
    }

    private fun buildFilename(alias: String, zpath: String, format: String): String {
        val filename = zpath.substringAfterLast('/').takeIf(String::isNotBlank) ?: alias
        return "$filename.$format"
    }

    private fun addAttachmentHeader(response: HttpServletResponse, filename: String) {
        response.addHeader(
                "Content-Disposition",
                "attachment; filename=$filename"
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createZNode(@PathVariable alias: String,
                    @RequestAttribute zpath: String,
                    @RequestParam(defaultValue = "false") overwrite: Boolean,
                    @RequestBody znode: ZNode): CreationConflicts {
        val zookeeperService = getService(alias)
        val conflicts = zookeeperService.createZNode(zpath, znode, overwrite)
        return CreationConflicts(conflicts)
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun updateZNode(@PathVariable alias: String,
                    @RequestAttribute zpath: String,
                    @RequestBody znode: ZNode) {
        val zookeeperService = getService(alias)
        zookeeperService.updateZNode(zpath, znode)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun deleteZNode(@PathVariable alias: String,
                    @RequestAttribute zpath: String) {
        val zookeeperService = getService(alias)
        zookeeperService.deleteZNode(zpath)
    }

    private fun getService(alias: String): ZookeeperService {
        val server = aliasRepository.findByAlias(alias) ?: throw AliasNotFoundException("Alias '$alias' was not found")
        return zookeeperServiceManager.getServiceFor(server)
    }
}
