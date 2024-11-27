package com.binarystack01.pix.domain.usecases.time

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Time {

    private fun formater(format: String = DEFAULT_DATE_FORMAT): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(format)
        return currentTime.format(formatter)
    }

    fun timeFormater(format: String): String = formater(format)

    fun timeNow(): String = formater("$DEFAULT_DATE_FORMAT $DEFAULT_TIME_FORMAT")

    companion object {
        private const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"
        private const val DEFAULT_TIME_FORMAT = "HH:mm:ss.SSS"
    }

}