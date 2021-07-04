package com.example.toshlautomator.application

import com.example.toshlautomator.domain.ToshlRepository
import com.example.toshlautomator.domain.Transaction
import org.springframework.stereotype.Service

@Service
class ConciliatorService(val toshlRepository: ToshlRepository) {

    fun registerTx(transaction: Transaction) {
        toshlRepository.getAccounts()
            .filter { it.name.contains(transaction.account) }
            .map { transaction.copy(account = it.id) }
            .map { toshlRepository.registerTx(it) }
            .subscribe()
    }

}