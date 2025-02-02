package com.example.urlshortener.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "url_table")
class UrlEntity {

    @Id
    lateinit var id: String

    @Column(name = "full_url")
    lateinit var fullUrl: String

    @Column(name = "created_at", insertable = false, updatable = false)
    var createdAt: Instant? = null

}
