package com.peterservice.zman.configuration

import com.google.common.base.Charsets
import com.peterservice.zman.api.exceptions.ZManException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.regex.Pattern
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

open class ZpathFilter : Filter {

    companion object {
        private val pattern = Pattern.compile("/api/zk(/[^/]+)(/?.*)")
        private val extraSlashesPattern = Pattern.compile("/+")
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val requestURI = (request as HttpServletRequest).requestURI
                .decode()
                .removeDuplicateSlashes()
                .removeSlashFromTheEnd()

        val matcher = pattern.matcher(requestURI)

        if (matcher.find()) {
            val zpath = matcher.group(2).takeIf(String::isNotBlank) ?: "/"
            request.setAttribute("zpath", zpath)
        }

        chain.doFilter(request, response)
    }

    private fun String.decode(): String {
        val escapedUri = replace("+", "%2B")
        try {
            return URLDecoder.decode(escapedUri, Charsets.UTF_8.name())
        } catch (e: UnsupportedEncodingException) {
            throw ZManException(e)
        }
    }

    private fun String.removeDuplicateSlashes() = extraSlashesPattern.matcher(this).replaceAll("/")

    private fun String.removeSlashFromTheEnd(): String {
        if (length > 1 && endsWith("/")) {
            return substring(0, length - 1)
        }
        return this
    }

    override fun init(filterConfig: FilterConfig?) = Unit

    override fun destroy() = Unit

}
