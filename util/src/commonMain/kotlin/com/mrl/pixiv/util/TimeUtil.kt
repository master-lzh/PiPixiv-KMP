package com.mrl.pixiv.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

fun currentTimeMillis() = Clock.System.now().toEpochMilliseconds()

fun convertUtcStringToLocalDateTime(utcString: String): String {
    val instant = Instant.parse(utcString)
    val localDateTime = instant.toLocalDateTime(TimeZone.UTC)
    val formatter = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        dayOfMonth()
        char(' ')
        hour()
        char(':')
        minute()
    }
    return localDateTime.format(formatter)
}