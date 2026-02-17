package firestorm.vuth.simplebank.service.impl

import firestorm.vuth.simplebank.dto.request.DepositRequest
import firestorm.vuth.simplebank.dto.request.TransferRequest
import firestorm.vuth.simplebank.dto.request.WithdrawRequest
import firestorm.vuth.simplebank.dto.response.TransactionResponse
import firestorm.vuth.simplebank.exception.BusinessRuleException
import firestorm.vuth.simplebank.exception.ResourceNotFoundException
import firestorm.vuth.simplebank.model.Enum.Currency
import firestorm.vuth.simplebank.model.Enum.TransactionStatus
import firestorm.vuth.simplebank.model.Enum.TransactionType
import firestorm.vuth.simplebank.repository.AccountRepo
import firestorm.vuth.simplebank.repository.UserRepo
import firestorm.vuth.simplebank.service.BankTransactionService
import firestorm.vuth.simplebank.service.TransferService
import firestorm.vuth.simplebank.utils.BankConfig
import jakarta.transaction.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransferServiceImpl(
    private val accountRepo: AccountRepo,
    private val bankTransactionService: BankTransactionService,
    private val userRepo: UserRepo
): TransferService {

    @Transactional
    override fun withdraw(accountNumber: Long, request: WithdrawRequest): TransactionResponse {
        if(request.amount < BigDecimal.ZERO) throw BusinessRuleException("Amount should be positive")
        if(request.amount > BigDecimal.valueOf(10000)) throw BusinessRuleException("Cannot withdraw more than 10000.")

        val row = accountRepo.debitBalance(accountNumber, request.amount)
        if (row == 0) throw ResourceNotFoundException("Insufficient balance or account not found")

        val bankAcc = accountRepo.findByAccountNumber(BankConfig.SYSTEM_ACCOUNT_NUMBER.toLong())
            ?: throw ResourceNotFoundException("No System Account found?!")

        return bankTransactionService.makeTransaction(
            senderAccount = bankAcc,
            receiverAccount = accountRepo.findByAccountNumber(accountNumber)!!,
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

        val debit = accountRepo.creditBalance(accountNumber, request.amount)
        if (debit == 0) throw ResourceNotFoundException("Account not found!")

        val bankAcc = accountRepo.findByAccountNumber(BankConfig.SYSTEM_ACCOUNT_NUMBER.toLong())
            ?: throw ResourceNotFoundException("No System Account found?!")

        return bankTransactionService.makeTransaction(
            senderAccount = bankAcc,
            receiverAccount = accountRepo.findByAccountNumber(accountNumber)!!,
            amount = request.amount,
            currency = Currency.valueOf(request.currency),
            type = TransactionType.DEPOSIT,
            status = TransactionStatus.SUCCESS,
        )
    }

    @Transactional
    override fun transfer(request: TransferRequest): TransactionResponse {
        val userEmail = SecurityContextHolder.getContext().authentication?.name
            ?: throw AccessDeniedException("User Not Found!")

        val user = userRepo.findByEmail(userEmail)
            ?: throw ResourceNotFoundException("User not found!")

        if(request.amount <= BigDecimal.ZERO) throw BusinessRuleException("Amount must be positive")
        if(request.amount > BigDecimal.valueOf(10000)) throw BusinessRuleException("Cannot transfer more than 10000.")
        if (request.senderAccount == request.receiverAccount)
            throw BusinessRuleException("Cannot transfer to the same account")

        val senderRow = accountRepo.debitBalanceForTransfer(request.senderAccount, request.amount, user.id)
        if (senderRow == 0) throw ResourceNotFoundException("Insufficient balance or sender not found")

        val receiverRow = accountRepo.creditBalance(request.receiverAccount, request.amount)
        if (receiverRow == 0) throw ResourceNotFoundException("No receiver found!")

        return bankTransactionService.makeTransaction(
            senderAccount = accountRepo.findByAccountNumber(request.senderAccount)!!,
            receiverAccount = accountRepo.findByAccountNumber(request.receiverAccount)!!,
            amount = request.amount,
            currency = request.currency,
            type = TransactionType.TRANSFER,
            status = TransactionStatus.SUCCESS,
        )
    }
}