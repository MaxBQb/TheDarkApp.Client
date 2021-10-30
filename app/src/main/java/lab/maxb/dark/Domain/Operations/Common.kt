package lab.maxb.dark.Domain.Operations

import java.security.MessageDigest
import java.util.*
import kotlin.text.Charsets.UTF_8

fun getUUID() = UUID.randomUUID().toString()

fun getTimestampNow() = System.currentTimeMillis() / 1000L

fun String.toSHA256()
    = MessageDigest.getInstance("SHA-256")
                   .digest(toByteArray(UTF_8))
                   .fold("") { str, it ->
                        str + "%02x".format(it)
                   }