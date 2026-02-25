package firestorm.vuth.simplebank.service.impl

import firestorm.vuth.simplebank.dto.request.DepositRequest
import firestorm.vuth.simplebank.dto.request.TransferRequest
import firestorm.vuth.simplebank.dto.request.WithdrawRequest
import firestorm.vuth.simplebank.dto.response.TransactionResponse
import firestorm.vuth.simplebank.exception.BusinessRuleException
import firestorm.vuth.simplebank.exception.ResourceNotFoundException
import firestorm.vuth.simplebank.model.Account
import firestorm.vuth.simplebank.model.Enum.Currency
import firestorm.vuth.simplebank.model.Enum.TransactionStatus
import firestorm.vuth.simplebank.model.Enum.TransactionType
import firestorm.vuth.simplebank.repository.AccountRepo
import firestorm.vuth.simplebank.repository.UserRepo
import firestorm.vuth.simplebank.service.BankTransactionService
import firestorm.vuth.simplebank.service.TransferService
import firestorm.vuth.simplebank.utils.BankConfig
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class TransferServiceImpl(
    private val accountRepo: AccountRepo,
    private val bankTransactionService: BankTransactionService,
    private val userRepo: UserRepo
): TransferService {

    @Transactional
    override fun withdraw(accountNumber: Long, request: WithdrawRequest): TransactionResponse {
        val account = accountRepo.findByAccountNumber(accountNumber) ?: throw BusinessRuleException("Account does not exist")

        if(request.amount < BigDecimal.ZERO) throw BusinessRuleException("Amount should be positive")
        if(request.amount > BigDecimal.valueOf(10000)) throw BusinessRuleException("Cannot withdraw more than 10000.")

        if(account.balance >= request.amount) {
            account.balance = account.balance.subtract(request.amount)
            accountRepo.save(account)
        } else {
            throw ResourceNotFoundException("Insufficient balance")
        }

        val bankAcc = accountRepo.findByAccountNumber(BankConfig.SYSTEM_ACCOUNT_NUMBER.toLong())
            ?: throw ResourceNotFoundException("No System Account found?!")

        return bankTransactionService.makeTransaction(
            senderAccount = bankAcc,
            receiverAccount = account,
            amount = request.amount,
            currency = Currency.valueOf(request.currency),
            type = TransactionType.WITHDRAW,
            status = TransactionStatus.SUCCESS,
        )
    }

    @Transactional
    override fun deposit(accountNumber: Long, request: DepositRequest): TransactionResponse {
        if(request.amount < BigDecimal.ZERO) throw BusinessRuleException("Amount should be positive")
        if(request.amount < BigDecimal.valueOf(5)) throw BusinessRuleException("Must deposit at least 5.")
        if(request.amount > BigDecimal.valueOf(10000)) throw BusinessRuleException("Cannot deposit more than 10000.")

        val account = accountRepo.findByAccountNumber(accountNumber)
            ?: throw ResourceNotFoundException("Account does not exist")

        account.balance = account.balance.add(request.amount)
        accountRepo.save(account)

        val bankAcc = accountRepo.findByAccountNumber(BankConfig.SYSTEM_ACCOUNT_NUMBER.toLong())
            ?: throw ResourceNotFoundException("No System Account found?!")

        return bankTransactionService.makeTransaction(
            senderAccount = bankAcc,
            receiverAccount = account,
            amount = request.amount,
            currency = Currency.valueOf(request.currency),
            type = TransactionType.DEPOSIT,
            status = TransactionStatus.SUCCESS,
        )
    }

    @Transactional
    override fun transfer(userId: String, request: TransferRequest): TransactionResponse {
        val user = userRepo.findByIdOrNull(UUID.fromString(userId))
            ?: throw ResourceNotFoundException("User not found!")

        val sender = accountRepo.findByAccountNumber(request.senderAccount)
            ?: throw ResourceNotFoundException("No sender found!")

        val receiver = accountRepo.findByAccountNumber(request.receiverAccount)
            ?: throw ResourceNotFoundException("No receiver found!")

        if(request.amount <= BigDecimal.ZERO) throw BusinessRuleException("Amount must be positive")
        if(request.amount > BigDecimal.valueOf(10000)) throw BusinessRuleException("Cannot transfer more than 10000.")
        if (request.senderAccount == request.receiverAccount)
            throw BusinessRuleException("Cannot transfer to the same account")
        if(sender.user?.id != user.id) throw BusinessRuleException("Wrong user account!")

        if(sender.balance >= request.amount) {
            sender.balance = sender.balance.subtract(request.amount)
            receiver.balance = receiver.balance.add(request.amount)
            accountRepo.saveAll(listOf(sender, receiver))
        } else {
            throw ResourceNotFoundException("Insufficient balance!")
        }

        return bankTransactionService.makeTransaction(
            senderAccount = sender,
            receiverAccount = receiver,
            amount = request.amount,
            currency = request.currency,
            type = TransactionType.TRANSFER,
            status = TransactionStatus.SUCCESS,
        )
    }
}