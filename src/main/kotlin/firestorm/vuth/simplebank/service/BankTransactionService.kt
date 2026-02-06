package firestorm.vuth.simplebank.service

import firestorm.vuth.simplebank.dto.response.TransactionResponse
import firestorm.vuth.simplebank.model.Account
import firestorm.vuth.simplebank.model.Enum.Currency
import firestorm.vuth.simplebank.model.Enum.TransactionStatus
import firestorm.vuth.simplebank.model.Enum.TransactionType
import firestorm.vuth.simplebank.model.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.math.BigDecimal

interface BankTransactionService {
    fun makeTransaction(
        senderAccount: Account,
        receiverAccount: Account,
        amount: BigDecimal,
        currency: Currency,
        type: TransactionType,
        status: TransactionStatus
    ): TransactionResponse

    fun getTransaction(email: String, pageable: Pageable): List<TransactionResponse>
}