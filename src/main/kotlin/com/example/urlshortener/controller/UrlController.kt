package com.example.urlshortener.controller

import com.example.urlshortener.service.UrlService
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.net.URI
import java.time.Duration
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/url")
class UrlController(
    private val urlService: UrlService,
    @Value("\${urlshortener.max-creation-rps:10}")
    private val maxCreationRps: Int
) {

    val creationRateLimit: Bucket = Bucket.builder()
        .addLimit(
            Bandwidth.classic(
                maxCreationRps.toLong(),
                Refill.intervally(maxCreationRps.toLong(), Duration.ofSeconds(1))
            )
        )
        .build()

    @PostMapping
    fun shortenUrl(
        @RequestBody url: String
    ): String {
        if (!creationRateLimit.tryConsume(1)) {
            throw ResponseStatusException(/* Too many requests */ 429, "Too many requests", null)
        }
        try {
            URI(url).toURL()
        } catch (e: Exception) {
            throw ResponseStatusException(SC_BAD_REQUEST, "Invalid url", e)
        }
        return urlService.create(url)
    }

    @Cacheable("cache_get_full_url")
    @GetMapping("{id}")
    fun findUrl(
        @PathVariable id: String
    ): ResponseEntity<Void> {
        val fullUrl = urlService.get(id) ?: throw ResponseStatusException(HttpServletResponse.SC_NOT_FOUND, "Invalid id: $id", null)

        return ResponseEntity.status(302)
            .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
            .location(URI.create(fullUrl))
            .build()
    }

}
