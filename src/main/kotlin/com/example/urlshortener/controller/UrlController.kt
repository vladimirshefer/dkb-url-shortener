package com.example.urlshortener.controller

import com.example.urlshortener.service.UrlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.net.URI

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

    @GetMapping("{id}")
    fun findUrl(
        @PathVariable id: String
    ): ResponseEntity<Void> {
        val fullUrl = urlService.get(id) ?: throw ResponseStatusException(HttpServletResponse.SC_NOT_FOUND, "Invalid id: $id", null)

        return ResponseEntity.status(302)
            .location(URI.create(fullUrl))
            .build()
    }

}
