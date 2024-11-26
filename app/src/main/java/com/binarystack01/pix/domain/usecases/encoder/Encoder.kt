package com.binarystack01.pix.domain.usecases.encoder

interface Encoder {
    fun encodeText(plainText: String): String
    fun decodeText(encodedText: String): String
}