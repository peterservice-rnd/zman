package com.peterservice.zman.api.repositories

import com.peterservice.zman.api.entities.ZServer
import org.springframework.data.repository.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
interface AliasRepository : Repository<ZServer, Long> {

    fun findByAlias(alias: String): ZServer?

    fun findAll(): Iterable<ZServer>

    fun save(server: ZServer): ZServer

    fun deleteByAlias(alias: String)

}
