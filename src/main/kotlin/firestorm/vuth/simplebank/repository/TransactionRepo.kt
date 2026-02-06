package firestorm.vuth.simplebank.repository

import firestorm.vuth.simplebank.model.Account
import firestorm.vuth.simplebank.model.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TransactionRepo: JpaRepository<Transaction, UUID> {
    fun findBySenderAccountInOrReceiverAccountIn(senderAccount: List<Account>, receiverAccount: List<Account>, pageable: Pageable): Page<Transaction>
}