package com.example.toshlautomator.infra.output.rest.adapters

import com.example.toshlautomator.domain.Transaction
import com.example.toshlautomator.infra.output.rest.Currency
import com.example.toshlautomator.infra.output.rest.Entry
import java.time.format.DateTimeFormatter

class ToshlAdapter {

    fun fromTransactionToEntry(transaction: Transaction): Entry {
        return Entry(
            amount = transaction.amount.toDouble() * -1,
            currency = Currency(),
            date = transaction.dateTime.format(DateTimeFormatter.ISO_DATE),
            desc = transaction.description,
            account = transaction.account,
            category = "66685380", // Pending category
            tags = listOf()
        )
    }
}