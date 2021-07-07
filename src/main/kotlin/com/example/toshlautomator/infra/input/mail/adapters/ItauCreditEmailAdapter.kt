package com.example.toshlautomator.infra.input.mail.adapters

import com.example.toshlautomator.domain.Transaction
import org.htmlcleaner.CleanerProperties
import org.htmlcleaner.DomSerializer
import org.htmlcleaner.HtmlCleaner
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

@Service
class ItauCreditEmailAdapter : EmailAdapter {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun isResponsible(mimeMessage: MimeMessage): Boolean {
        return mimeMessage.subject.contains("Notificaciones Itau")
    }

    override fun adapt(mimeMessage: MimeMessage): Transaction {

        log.info("Itau tx received!")

        val data = (mimeMessage.content as MimeMultipart).getBodyPart(1).content as String

        val tagNode = HtmlCleaner().clean(data)
        val doc = DomSerializer(CleanerProperties()).createDOM(tagNode)

        val xpath = XPathFactory.newInstance().newXPath()

        val rawAmount = xpath.evaluate("/html/body/table[2]/tr[1]/td[2]/text()", doc, XPathConstants.STRING) as String
        val amount = rawAmount.replace(Regex("\\D"), "").toLong()

        val rawDate =
            (xpath.evaluate("/html/body/table[2]/tr[2]/td[2]/text()", doc, XPathConstants.STRING) as String).trim()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        val txDateTime = LocalDateTime.parse(rawDate, dateFormatter)


        val rawDescription = xpath.evaluate("/html/body/table[1]/tr/td/text()", doc, XPathConstants.STRING) as String
        val account = Regex("""\**\d{4}""").find(rawDescription)!!.groupValues.reduce { x, y -> x + y }.replace("*", "")


        return Transaction(account, amount, "$rawDescription at $rawDate", txDateTime)
    }


}