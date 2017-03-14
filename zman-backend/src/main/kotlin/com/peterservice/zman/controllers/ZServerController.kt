package com.peterservice.zman.controllers

import com.peterservice.zman.api.entities.ZServer
import com.peterservice.zman.api.exceptions.AliasAlreadyExistsException
import com.peterservice.zman.api.exceptions.AliasNotFoundException
import com.peterservice.zman.api.repositories.AliasRepository
import com.peterservice.zman.api.zookeeper.ZookeeperServiceManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/servers")
class ZServerController {

    @Autowired
    private lateinit var aliasRepository: AliasRepository

    @Autowired
    private lateinit var zookeeperServiceManager: ZookeeperServiceManager

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getServers(): Iterable<ZServer> = aliasRepository.findAll()

    @GetMapping("/{alias}")
    @ResponseStatus(HttpStatus.OK)
    fun getServer(@PathVariable alias: String): ZServer {
        return aliasRepository.findByAlias(alias) ?: throw AliasNotFoundException(buildNotFoundMessage(alias))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addAlias(@RequestBody server: ZServer) {
        saveServer(server)
    }

    @PutMapping("/{alias}")
    @ResponseStatus(HttpStatus.OK)
    fun updateAlias(@PathVariable alias: String, @RequestBody server: ZServer) {
        val oldServer = aliasRepository.findByAlias(alias) ?: throw AliasNotFoundException(buildNotFoundMessage(alias))
        server.id = oldServer.id
        saveServer(server)
        zookeeperServiceManager.invalidateCacheFor(server)
    }

    @DeleteMapping("/{alias}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteAlias(@PathVariable alias: String) {
        val server = aliasRepository.findByAlias(alias) ?: throw AliasNotFoundException(buildNotFoundMessage(alias))
        aliasRepository.deleteByAlias(server.alias)
        zookeeperServiceManager.invalidateCacheFor(server)
    }

    private fun saveServer(server: ZServer) {
        try {
            aliasRepository.save(server)
        } catch (e: DataIntegrityViolationException) {
            throw AliasAlreadyExistsException("Alias ${server.alias} already exists")
        }
    }

    private fun buildNotFoundMessage(alias: String): String {
        return "Alias '$alias' was not found"
    }

}
