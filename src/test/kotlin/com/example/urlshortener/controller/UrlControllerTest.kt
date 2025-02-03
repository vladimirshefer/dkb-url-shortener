package com.example.urlshortener.controller

import com.example.urlshortener.id_generator.IdGenerator
import com.example.urlshortener.service.UrlService
import jakarta.servlet.http.HttpServletResponse.*
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings.Redirects
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import kotlin.math.absoluteValue
import kotlin.random.Random


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
internal class UrlControllerTest {

    @Autowired
    lateinit var urlController: UrlController

    @MockitoBean
    lateinit var idGenerator: IdGenerator

    @MockitoSpyBean
    lateinit var urlService: UrlService

    @Autowired
    lateinit var rest: TestRestTemplate

    @BeforeEach
    fun setUp() {
        // disable following redirects
        rest = rest.withRequestFactorySettings(ClientHttpRequestFactorySettings(Redirects.DONT_FOLLOW, null, null, null))
    }

    @Test
    fun happy_path() {
        val testId = Random.nextInt().absoluteValue.toString()
        doReturn(testId).`when`(idGenerator).generate()
        val fullUrl = "http://www.mylongurl.com/foo"

        val postResponse = rest.postForEntity<String>(
            "/url",
            fullUrl
        )
        assertEquals(SC_OK, postResponse.statusCode.value())
        assertEquals(testId, postResponse.body)

        val getResponse = rest.getForEntity<Unit>("/url/$testId")
        assertEquals(SC_MOVED_TEMPORARILY, getResponse.statusCode.value())
        assertEquals(fullUrl, getResponse.headers.location.toString())
    }

    @Test
    fun illegal_url_should_fail() {
        val testId = Random.nextInt().absoluteValue.toString()
        doReturn(testId).`when`(idGenerator).generate()
        val fullUrl = "illegal url"

        val postResponse = rest.postForEntity<String>(
            "/url",
            fullUrl
        )
        assertEquals(SC_BAD_REQUEST, postResponse.statusCode.value())

        val getResponse = rest.getForEntity<Unit>("/url/$testId")
        assertEquals(SC_NOT_FOUND, getResponse.statusCode.value())
        assertNull(getResponse.headers.location)
    }

    @Test
    fun cache() {
        val testId = Random.nextInt().absoluteValue.toString()
        doReturn(testId).`when`(idGenerator).generate()
        val fullUrl = "http://www.mylongurl.com/foo"

        run {
            val getResponse = rest.getForEntity<Unit>("/url/$testId")
            assertEquals(SC_NOT_FOUND, getResponse.statusCode.value())
            assertNull(getResponse.headers.location)
        }
        reset(urlService)

        val postResponse = rest.postForEntity<String>(
            "/url",
            fullUrl
        )
        assertEquals(SC_OK, postResponse.statusCode.value())
        assertEquals(testId, postResponse.body)

        run {
            val getResponse = rest.getForEntity<Unit>("/url/$testId")
            assertEquals(SC_MOVED_TEMPORARILY, getResponse.statusCode.value())
            assertEquals(fullUrl, getResponse.headers.location.toString())
        }
        run {
            val getResponse = rest.getForEntity<Unit>("/url/$testId")
            assertEquals(SC_MOVED_TEMPORARILY, getResponse.statusCode.value())
            assertEquals(fullUrl, getResponse.headers.location.toString())
        }
        verify(urlService).get(testId) // cache should handle second request
    }
}
