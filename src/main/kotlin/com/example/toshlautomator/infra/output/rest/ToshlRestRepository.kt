package com.example.toshlautomator.infra.output.rest

import com.example.toshlautomator.domain.Accounts
import com.example.toshlautomator.domain.ToshlRepository
import com.example.toshlautomator.domain.Transaction
import com.example.toshlautomator.infra.output.rest.adapters.ToshlAdapter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ToshlRestRepository(val webClient: WebClient) : ToshlRepository {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun registerTx(transaction: Transaction) {
        val entry = Mono.just(transaction)
            .map { ToshlAdapter().fromTransactionToEntry(it) }

        webClient.post()
            .uri("/entries")
            .body(entry, Entry::class.java)
            .retrieve()
            .bodyToMono(String::class.java)
            .subscribe {}

        log.info("Transaction registered! $transaction")
    }

    override fun getAccounts(): Flux<Accounts> {
        return webClient.get()
            .uri("/accounts")
            .retrieve()
            .bodyToFlux(Accounts::class.java)
    }
}