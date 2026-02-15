package ngo.friendship.mhealth.dc.utils

// commonMain
expect fun String.md5(): String


fun String.md52(): String {
    val message = encodeToByteArray()
    val bitLength = message.size.toLong() shl 3

    // Calculate padding more efficiently
    val paddingLength = (55 - message.size and 63)
    val padded = ByteArray(message.size + paddingLength + 9)

    message.copyInto(padded)
    padded[message.size] = 0x80.toByte()

    // Write bit length in little-endian
    var len = bitLength
    for (i in padded.size - 8 until padded.size) {
        padded[i] = len.toByte()
        len = len ushr 8
    }

    var a = 0x67452301
    var b = 0xefcdab89.toInt()
    var c = 0x98badcfe.toInt()
    var d = 0x10325476

    // Precomputed constants
    val k = intArrayOf(
        0xd76aa478.toInt(), 0xe8c7b756.toInt(), 0x242070db, 0xc1bdceee.toInt(),
        0xf57c0faf.toInt(), 0x4787c62a, 0xa8304613.toInt(), 0xfd469501.toInt(),
        0x698098d8, 0x8b44f7af.toInt(), 0xffff5bb1.toInt(), 0x895cd7be.toInt(),
        0x6b901122, 0xfd987193.toInt(), 0xa679438e.toInt(), 0x49b40821,
        0xf61e2562.toInt(), 0xc040b340.toInt(), 0x265e5a51, 0xe9b6c7aa.toInt(),
        0xd62f105d.toInt(), 0x02441453, 0xd8a1e681.toInt(), 0xe7d3fbc8.toInt(),
        0x21e1cde6, 0xc33707d6.toInt(), 0xf4d50d87.toInt(), 0x455a14ed,
        0xa9e3e905.toInt(), 0xfcefa3f8.toInt(), 0x676f02d9, 0x8d2a4c8a.toInt(),
        0xfffa3942.toInt(), 0x8771f681.toInt(), 0x6d9d6122, 0xfde5380c.toInt(),
        0xa4beea44.toInt(), 0x4bdecfa9, 0xf6bb4b60.toInt(), 0xbebfbc70.toInt(),
        0x289b7ec6, 0xeaa127fa.toInt(), 0xd4ef3085.toInt(), 0x04881d05,
        0xd9d4d039.toInt(), 0xe6db99e5.toInt(), 0x1fa27cf8, 0xc4ac5665.toInt(),
        0xf4292244.toInt(), 0x432aff97, 0xab9423a7.toInt(), 0xfc93a039.toInt(),
        0x655b59c3, 0x8f0ccc92.toInt(), 0xffeff47d.toInt(), 0x85845dd1.toInt(),
        0x6fa87e4f, 0xfe2ce6e0.toInt(), 0xa3014314.toInt(), 0x4e0811a1,
        0xf7537e82.toInt(), 0xbd3af235.toInt(), 0x2ad7d2bb, 0xeb86d391.toInt()
    )

    val s = intArrayOf(
        7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
        5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
        4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
        6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
    )

    val m = IntArray(16)

    for (i in padded.indices step 64) {
        // Parse message block more efficiently
        for (j in 0 until 16) {
            val offset = i + (j shl 2)
            m[j] = (padded[offset].toInt() and 0xff) or
                    ((padded[offset + 1].toInt() and 0xff) shl 8) or
                    ((padded[offset + 2].toInt() and 0xff) shl 16) or
                    ((padded[offset + 3].toInt() and 0xff) shl 24)
        }

        var aa = a
        var bb = b
        var cc = c
        var dd = d

        // Unrolled rounds for better performance
        repeat(16) { j ->
            val f = (bb and cc) or (bb.inv() and dd)
            val temp = dd
            dd = cc
            cc = bb
            bb += (aa + f + k[j] + m[j]).rotateLeft(s[j])
            aa = temp
        }

        repeat(16) { j ->
            val idx = j + 16
            val f = (dd and bb) or (dd.inv() and cc)
            val g = (5 * idx + 1) and 15
            val temp = dd
            dd = cc
            cc = bb
            bb += (aa + f + k[idx] + m[g]).rotateLeft(s[idx])
            aa = temp
        }

        repeat(16) { j ->
            val idx = j + 32
            val f = bb xor cc xor dd
            val g = (3 * idx + 5) and 15
            val temp = dd
            dd = cc
            cc = bb
            bb += (aa + f + k[idx] + m[g]).rotateLeft(s[idx])
            aa = temp
        }

        repeat(16) { j ->
            val idx = j + 48
            val f = cc xor (bb or dd.inv())
            val g = (7 * idx) and 15
            val temp = dd
            dd = cc
            cc = bb
            bb += (aa + f + k[idx] + m[g]).rotateLeft(s[idx])
            aa = temp
        }

        a += aa
        b += bb
        c += cc
        d += dd
    }

    // More efficient hex conversion
    return buildString(32) {
        listOf(a, b, c, d).forEach {
            val hex = it.toUInt().toString(16)
            append("0".repeat(8 - hex.length))
            append(hex)
        }
    }
}
