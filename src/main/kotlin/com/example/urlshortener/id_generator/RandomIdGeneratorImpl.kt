package com.example.urlshortener.id_generator

import java.util.*
import java.util.random.RandomGenerator


/**
 * A random ID generator that creates a string of specified length using a given character set and random generator.
 * This implementation is configurable for character set, random generator, and ID length.
 *
 * Default configuration provides ~2e+14 unique ids.
 */
class RandomIdGeneratorImpl(
    /**
     * The alphabet to be used for generating the IDs.
     * Defaults to a mix of Latin letters and digits.
     */
    private val alphabet: String = ALPHABET_ALL,
    /**
     * The random generator used to select characters from the alphabet.
     */
    private val random: RandomGenerator = Random(),
    /**
     * The length of the generated ID.
     * Defaults to 8.
     */
    private val length: Int = 8,
) : IdGenerator {

    /**
     * Generates a random ID of the configured length using the given alphabet and random generator.
     *
     * @return A randomly generated ID as a string.
     */
    override fun generate(): String {
        return String(CharArray(length) { alphabet[random.nextInt(alphabet.length)] })
    }

    companion object {
        /**
         * The lowercase Latin alphabet (a-z).
         */
        const val ALPHABET_LATIN_SMALL = "abcdefghijklmnopqrstuvwxyz"

        /**
         * The uppercase Latin alphabet (A-Z).
         */
        const val ALPHABET_LATIN_BIG = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        /**
         * The numeric digits (0-9).
         */
        const val ALPHABET_DIGITS = "0123456789"

        /**
         * A combination of lowercase Latin alphabet, uppercase Latin alphabet, and numeric digits.
         */
        const val ALPHABET_ALL = ALPHABET_LATIN_SMALL + ALPHABET_LATIN_BIG + ALPHABET_DIGITS
    }
}
