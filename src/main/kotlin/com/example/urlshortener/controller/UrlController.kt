package com.example.urlshortener.controller

import com.example.urlshortener.id_generator.IdGenerator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/url")
class UrlController(
    private val idGenerator: IdGenerator
) {

    @PostMapping
    fun shortenUrl(
        @RequestBody url: String?
    ): String {
        return idGenerator.generate()
    }

}
