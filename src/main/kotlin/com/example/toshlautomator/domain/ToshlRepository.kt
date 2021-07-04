package com.example.toshlautomator.domain

import reactor.core.publisher.Flux

interface ToshlRepository {
    fun registerTx(transaction: Transaction)
    fun getAccounts(): Flux<Accounts>
}