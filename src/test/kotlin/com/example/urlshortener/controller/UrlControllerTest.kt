package com.example.urlshortener.controller

import com.example.urlshortener.id_generator.IdGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
internal class UrlControllerTest {

    @Autowired
    lateinit var urlController: UrlController

    @MockitoBean
    lateinit var idGenerator: IdGenerator

    @Autowired
    lateinit var rest: TestRestTemplate

    @Test
    fun name() {
        doReturn("abc").`when`(idGenerator).generate()

        val response = rest.postForObject(
            "/url",
            "http://www.mylongurl.com/foo",
            String::class.java
        )
        Assertions.assertEquals("abc", response)
    }
}
