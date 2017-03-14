package com.peterservice.zman.core.zookeeper.commands

import com.google.common.base.Charsets.UTF_8
import com.peterservice.zman.api.exceptions.ZNodeNotFoundException
import java.util.regex.Pattern

object CommandUtils {

    private val XML_UNSUPPORTED_CHARACTERS = Pattern.compile("[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD\uD800\uDC00-\uDBFF\uDFFF]")

    internal fun throwNotFoundException(path: String): Nothing = throw ZNodeNotFoundException(buildNotFoundMessage(path))

    private fun buildNotFoundMessage(path: String) = "ZNode '$path' was not found"

    internal fun valueToBytes(value: String?) = value?.toByteArray(UTF_8)

    internal fun bytesToValue(bytes: ByteArray?) = bytes?.toString(UTF_8)?.removeXmlUnsupportedCharacters()

    internal fun makeZPath(path: String, key: String) = (path.takeIf { it != "/" } ?: "") + "/" + key

    internal fun extractParentPath(path: String) = path.substringBeforeLast("/").takeIf(String::isNotBlank) ?: "/"

    private fun String.removeXmlUnsupportedCharacters() = XML_UNSUPPORTED_CHARACTERS.matcher(this).replaceAll("")
}
