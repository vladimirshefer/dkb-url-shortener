package com.example.urlshortener.util

fun <R> retry(times: Int, block: () -> R): Result<R> {
    var retries = times
    var lastException: Exception? = null
    while (retries-- > 0) {
        try {
            return Result.success(block())
        } catch (e: Exception) {
            lastException = e
        }
    }
    return Result.failure(lastException ?: IllegalStateException("Out of retries"))
}
