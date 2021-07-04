package com.example.toshlautomator.infra.input.mail.adapters

import com.example.toshlautomator.domain.Transaction
import javax.mail.internet.MimeMessage

interface EmailAdapter {

    fun isResponsible(mimeMessage: MimeMessage): Boolean

    fun adapt(mimeMessage: MimeMessage): Transaction
}