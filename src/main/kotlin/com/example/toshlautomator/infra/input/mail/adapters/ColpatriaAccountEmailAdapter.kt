package com.example.toshlautomator.infra.input.mail.adapters

import com.example.toshlautomator.domain.Transaction
import org.htmlcleaner.CleanerProperties
import org.htmlcleaner.DomSerializer
import org.htmlcleaner.HtmlCleaner
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

@Service
class ColpatriaAccountEmailAdapter : EmailAdapter {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun isResponsible(mimeMessage: MimeMessage): Boolean {
        return mimeMessage.subject.contains("Scotiabank Colpatria en Linea")
    }

    override fun adapt(mimeMessage: MimeMessage): Transaction {

        log.info("Colpatria tx received!")

        val data = mimeMessage.content.toString()

        val tagNode = HtmlCleaner().clean(data)
        val doc = DomSerializer(CleanerProperties()).createDOM(tagNode)

        val xpath = XPathFactory.newInstance().newXPath()

        val rawAmount = xpath.evaluate(".//table/tbody[1]/tr[4]/td[2]", doc, XPathConstants.STRING) as String
        val amount = rawAmount.replace(Regex("\\D"), "").toLong()

        val rawDate =
            (xpath.evaluate(".//table/tbody[1]/tr[1]/td[2]", doc, XPathConstants.STRING) as String).trim()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss")
        val txDateTime = LocalDateTime.parse(rawDate, dateFormatter)


        val rawDescription = xpath.evaluate(".//table/tbody[1]/tr[5]/td[2]", doc, XPathConstants.STRING) as String

        val account = xpath.evaluate(".//table/tbody[1]/tr[3]/td[2]", doc, XPathConstants.STRING) as String


        return Transaction(account, amount, "$rawDescription at $rawDate", txDateTime)
    }
}