package com.example.urlshortener.id_generator

import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class RandomIdGeneratorImplTest {
    @Test
    fun test1() {
        val generator = RandomIdGeneratorImpl(
            alphabet = RandomIdGeneratorImpl.ALPHABET_ALL,
            random = Random(0),
            length = 8
        )

        assertEquals("ccZlnhbf", generator.generate())
    }

    @Test
    fun test2() {
        val generator = RandomIdGeneratorImpl(
            alphabet = RandomIdGeneratorImpl.ALPHABET_DIGITS,
            random = Random(0),
            length = 10
        )

        assertEquals("0897531194", generator.generate())
    }
}