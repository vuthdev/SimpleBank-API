package firestorm.vuth.simplebank.service.impl

import firestorm.vuth.simplebank.mapper.toResponse
import firestorm.vuth.simplebank.dto.response.TransactionResponse
import firestorm.vuth.simplebank.exception.ResourceNotFoundException
import firestorm.vuth.simplebank.mapper.toResponseOrNull
import firestorm.vuth.simplebank.model.Account
import firestorm.vuth.simplebank.model.Enum.Currency
import firestorm.vuth.simplebank.model.Enum.TransactionStatus
import firestorm.vuth.simplebank.model.Enum.TransactionType
import firestorm.vuth.simplebank.model.Transaction
import firestorm.vuth.simplebank.repository.TransactionRepo
import firestorm.vuth.simplebank.repository.UserRepo
import firestorm.vuth.simplebank.service.BankTransactionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class BankTransactionServiceImpl(
    private val transactionRepo: TransactionRepo,
    private val userRepo: UserRepo,
): BankTransactionService {
    override fun makeTransaction(
        senderAccount: Account,
        receiverAccount: Account,
        amount: BigDecimal,
        currency: Currency,
        type: TransactionType,
        status: TransactionStatus
    ): TransactionResponse {
        val transaction = Transaction(
            senderAccount = senderAccount,
            receiverAccount = receiverAccount,
            amount = amount,
            currency = currency,
            type = type,
            status = status
        )
        return transactionRepo.save(transaction).toResponse()
    }

    override fun getTransaction(email: String, pageable: Pageable): List<TransactionResponse> {
        val user = userRepo.findByEmail(email) ?: throw ResourceNotFoundException("User $email not found")

        val finalPage = if (pageable.sort.isUnsorted) {
            PageRequest.of(pageable.pageNumber, pageable.pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))
        } else pageable

        val accounts = user.bankAccounts
        if (accounts.isEmpty()) return emptyList()

        val transactions: Page<Transaction> = transactionRepo.findBySenderAccountInOrReceiverAccountIn(accounts, accounts, finalPage)
        return transactions.content.toResponse()
    }
}