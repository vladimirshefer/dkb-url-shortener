package com.example.urlshortener.controller

import com.example.urlshortener.service.UrlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.net.URI
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/url")
class UrlController(
    private val urlService: UrlService
) {

    @PostMapping
    fun shortenUrl(
        @RequestBody url: String
    ): String {
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
