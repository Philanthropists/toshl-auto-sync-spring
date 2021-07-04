package com.example.toshlautomator.infra.output.rest.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ToshlWebClientConfig(
    @Value("\${toshl.http.token}")
    val token: String
) {

    @Bean
    fun createToshlWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://api.toshl.com/")
            .defaultHeaders {
                it[HttpHeaders.CONTENT_TYPE] = listOf(MediaType.APPLICATION_JSON_VALUE)
                it[HttpHeaders.AUTHORIZATION] = listOf("Bearer $token")
            }
            .build()
    }
}