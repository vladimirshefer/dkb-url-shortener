package com.example.urlshortener.repository

import com.example.urlshortener.entity.UrlEntity
import org.springframework.data.repository.ListCrudRepository

interface UrlRepository : ListCrudRepository<UrlEntity, String>
