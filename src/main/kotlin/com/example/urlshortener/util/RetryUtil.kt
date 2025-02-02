package com.example.urlshortener.util

import com.example.urlshortener.entity.UrlEntity

fun <R> retry(times: Int, block: () -> R): R {
    var retries = times
    var lastException: Exception? = null
    while (retries-- > 0) {
        try {
            return block()
        } catch (e: Exception) {
            lastException = e
        }
    }
    throw lastException ?: IllegalStateException("Out of retries")
}
