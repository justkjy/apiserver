package org.zerock.apiserver.controller.formatter

import org.springframework.format.Formatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class LocalDateFormatter : Formatter<LocalDate> {

    override fun print(`object`: LocalDate, locale: Locale): String {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(`object`)
    }

    override fun parse(text: String, locale: Locale): LocalDate {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}