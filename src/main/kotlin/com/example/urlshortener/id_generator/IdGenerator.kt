package com.example.urlshortener.id_generator

fun interface IdGenerator {

    /**
     * Generates an identifier.
     *It is suggested that ids have a large set and are well distributed.
     *
     * @return an ID as a string.
     */
    fun generate(): String

}
