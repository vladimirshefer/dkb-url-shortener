package com.example.urlshortener.repository

import com.example.urlshortener.entity.UrlEntity
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.ListCrudRepository

interface UrlRepository : ListCrudRepository<UrlEntity, String> {

    @Query(value = "INSERT INTO url_table (id, full_url) VALUES (:#{#urlEntity.id}, :#{#urlEntity.fullUrl})", nativeQuery = true)
    @Modifying
    @Transactional
    fun insert(urlEntity: UrlEntity)

}
