package com.example.toshlautomator.infra.input.mail.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.mail.ImapMailReceiver
import org.springframework.integration.mail.dsl.Mail
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

@Configuration
@PropertySource("classpath:application.properties")
class MailConfig(
    @Value("\${toshl.email.receiver.password}")
    val receiverPassword: String,

    @Value("\${toshl.email.receiver.host}")
    val receiverHost: String,

    @Value("\${toshl.email.receiver.user}")
    val receiverUser: String,

    @Value("\${toshl.email.receiver.port}")
    val receiverPort: String,

    @Value("\${toshl.email.receiver.mailbox}")
    val receiverBox: String,

    @Value("\${toshl.email.debug}")
    val debug: String
) {

    companion object {
        const val IMAP_CHANNEL = "imapChannel"
    }


    @Bean
    fun receiver(): ImapMailReceiver {
        val receiver = ImapMailReceiver(imapUrl)
        receiver.setJavaMailProperties(javaMailProperties())
        receiver.setCancelIdleInterval(30)
        receiver.isShouldMarkMessagesAsRead = false
        receiver.setSimpleContent(true)
        return receiver
    }

    private val imapUrl: String
        get() = ("imaps://" + receiverUser + ":"
                + URLEncoder.encode(receiverPassword, StandardCharsets.UTF_8.toString()) + "@"
                + receiverHost + ":" + receiverPort + "/" + receiverBox)

    @Bean
    @Qualifier(value = "imapMailFlow")
    fun imapMailFlow(): IntegrationFlow {
        return IntegrationFlows.from(Mail.imapIdleAdapter(receiver())).channel(IMAP_CHANNEL).get()
    }

    private fun javaMailProperties(): Properties {
        val javaMailProperties = Properties()
        javaMailProperties.setProperty(
            "mail.imap.socketFactory.class",
            "javax.net.ssl.SSLSocketFactory"
        )
        javaMailProperties.setProperty("mail.imap.socketFactory.fallback", "false")
        javaMailProperties.setProperty("mail.store.protocol", "imaps")
        javaMailProperties.setProperty("mail.debug", debug)
        return javaMailProperties
    }


}