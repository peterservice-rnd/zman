package com.peterservice.zman.configuration

import com.peterservice.zman.api.zookeeper.ZookeeperService
import net.sf.ehcache.Ehcache
import net.sf.ehcache.Element
import net.sf.ehcache.event.CacheEventListener

class ZookeeperServiceCacheEventListener : CacheEventListener {

    override fun notifyElementRemoved(cache: Ehcache, element: Element) = closeCuratorService(element)

    override fun notifyElementExpired(cache: Ehcache, element: Element) = closeCuratorService(element)

    override fun notifyElementEvicted(cache: Ehcache, element: Element) = closeCuratorService(element)

    private fun closeCuratorService(element: Element) = (element.objectValue as ZookeeperService).close()

    override fun notifyElementPut(cache: Ehcache, element: Element) = Unit

    override fun notifyElementUpdated(cache: Ehcache, element: Element) = Unit

    override fun notifyRemoveAll(cache: Ehcache) = Unit

    override fun dispose() = Unit

    override fun clone(): Any = throw CloneNotSupportedException("ZookeeperServiceCacheEventListener can not be cloned")
}