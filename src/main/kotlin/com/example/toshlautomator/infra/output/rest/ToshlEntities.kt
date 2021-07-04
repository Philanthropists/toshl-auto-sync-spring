package com.example.toshlautomator.infra.output.rest


data class Entry(
    val amount: Double,
    val currency: Currency,
    val date: String,
    val desc: String,
    val account: String,
    val category: String,
    val tags: List<String>
)

data class Currency(
    val code: String = "COP",
    val rate: Int = 1,
    val fixed: Boolean = false
)


