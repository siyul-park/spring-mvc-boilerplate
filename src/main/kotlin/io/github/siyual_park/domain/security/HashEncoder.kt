package io.github.siyual_park.domain.security

import java.security.MessageDigest
import kotlin.experimental.and

private val digits = "0123456789ABCDEF".toCharArray()

object HashEncoder {
    fun encode(value: String, algorithm: String): ByteArray {
        val messageDigest = MessageDigest.getInstance(algorithm)
        messageDigest.update(value.toByteArray())

        return messageDigest.digest()
    }

    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = digits[v.ushr(4)]
            hexChars[i * 2 + 1] = digits[v and 0x0F]
        }

        return String(hexChars)
    }
}
