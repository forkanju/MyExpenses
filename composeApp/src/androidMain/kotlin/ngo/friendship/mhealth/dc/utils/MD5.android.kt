package ngo.friendship.mhealth.dc.utils

import java.math.BigInteger
import java.security.MessageDigest


actual fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray(Charsets.UTF_8))
    return BigInteger(1, digest)
        .toString(16)
        .padStart(32, '0')
}