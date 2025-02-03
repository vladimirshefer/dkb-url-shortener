package com.example.urlshortener

import com.example.urlshortener.id_generator.IdGenerator
import com.example.urlshortener.id_generator.RandomIdGeneratorImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean


@SpringBootApplication
@EnableCaching
class Application {

    companion object {
        @JvmStatic
        @Bean
        fun idGenerator(
            @Value("\${urlshortener.id-generator.length:8}")
            length: Int
        ): IdGenerator = RandomIdGeneratorImpl(
            length = length,
        )
    }

}

fun main() {
    SpringApplication.run(Application::class.java)
}
