package com.example.toshlautomator.domain

import java.time.LocalDateTime

data class Transaction(
    val account: String,
    val amount: Long,
    val description: String,
    val dateTime: LocalDateTime
)


data class Accounts(val id: String, val name: String)