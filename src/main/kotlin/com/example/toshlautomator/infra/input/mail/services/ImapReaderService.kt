package com.example.toshlautomator.infra.input.mail.services

import com.example.toshlautomator.application.ConciliatorService
import com.example.toshlautomator.infra.input.mail.adapters.EmailAdapter
import org.springframework.context.annotation.Bean
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.MessageHandler
import org.springframework.stereotype.Service
import javax.mail.internet.MimeMessage

@Service
class ImapReaderService(
    val emailAdapter: List<EmailAdapter>,
    val conciliatorService: ConciliatorService
) {

    @Bean
    @ServiceActivator(inputChannel = "imapChannel")
    fun readMessage(): MessageHandler {
        return MessageHandler { message ->
            val mimeMessage = message.payload as MimeMessage
            processMessage(mimeMessage)
        }
    }

    private fun processMessage(mimeMessage: MimeMessage) {
        emailAdapter.filter { it.isResponsible(mimeMessage) }
            .map { it.adapt(mimeMessage) }
            .map { conciliatorService.registerTx(it) }

    }

}