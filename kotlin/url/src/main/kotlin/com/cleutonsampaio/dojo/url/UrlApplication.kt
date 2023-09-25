package com.cleutonsampaio.dojo.url

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = arrayOf(DataSourceAutoConfiguration::class))
class UrlApplication

fun main(args: Array<String>) {
	runApplication<UrlApplication>(*args)
}
