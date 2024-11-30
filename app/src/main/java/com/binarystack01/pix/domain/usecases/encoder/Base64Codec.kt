package com.binarystack01.pix.domain.usecases.encoder

import android.util.Base64

class Base64Codec : Encoder {
    override fun encodeText(plainText: String): String {
        return Base64.encodeToString(plainText.toByteArray(), Base64.DEFAULT)
    }

    override fun decodeText(encodedText: String): String {
        return Base64.decode(encodedText, Base64.DEFAULT).toString()
    }
}