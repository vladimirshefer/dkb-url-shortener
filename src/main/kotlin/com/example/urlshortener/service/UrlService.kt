package com.example.urlshortener.service

import com.example.urlshortener.entity.UrlEntity
import com.example.urlshortener.id_generator.IdGenerator
import com.example.urlshortener.repository.UrlRepository
import com.example.urlshortener.util.retry
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UrlService(
    private val idGenerator: IdGenerator,
    private val urlRepository: UrlRepository,
) {

    fun create(fullUrl: String): String {
        // It is extremely unlikely to fail.
        // Probability of success = 1 - (number_of_used_ids / number_of_all_ids) ^ retry_times
        // Even having 10Krps for 100 year (gives ~3e+13 saved ids)
        // with default configuration (8 chars from alphabet of 62 = ~2e+14 ids)
        // gives ~85% chance of successful insert for each attempt
        // therefore >99,999999% chance after 10 retries.
        return retry(RETRY_TIMES) {
            val id = idGenerator.generate()
            urlRepository.insert(
                UrlEntity().also {
                    it.id = id
                    it.fullUrl = fullUrl
                }
            )
            id
        }
            .onFailure { LOG.error("Error saving URL", it) }
            .getOrThrow()
    }

    fun get(id: String): String? {
        return urlRepository.findById(id).map { it.fullUrl }.orElse(null)
    }

    companion object {
        private const val RETRY_TIMES = 10

        private val LOG = LoggerFactory.getLogger(UrlService::class.java)
    }
}
