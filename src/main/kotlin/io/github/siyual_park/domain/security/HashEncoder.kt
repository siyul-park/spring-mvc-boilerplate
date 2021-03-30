package io.github.siyual_park.domain.security

import java.security.MessageDigest

private val hexArray = "0123456789abcdef".toCharArray()

object HashEncoder {
    fun encode(value: String, algorithm: String): ByteArray {
        val messageDigest = MessageDigest.getInstance(algorithm)
        messageDigest.update(value.toByte())

        return messageDigest.digest()
    }

    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        bytes.forEachIndexed { byte, i ->
            val v = byte.and(0xFF)
            hexChars[i * 2] = hexArray[v.shr(4)]
            hexChars[i * 2 + 1] = hexArray[v.and(0x0F)]
        }
        return String(hexChars)
    }
}
