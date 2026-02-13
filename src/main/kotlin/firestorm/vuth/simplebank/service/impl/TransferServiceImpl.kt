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
import firestorm.vuth.simplebank.model.Transaction
import firestorm.vuth.simplebank.repository.AccountRepo
import firestorm.vuth.simplebank.repository.UserRepo
import firestorm.vuth.simplebank.service.BankTransactionService
import firestorm.vuth.simplebank.service.TransferService
import firestorm.vuth.simplebank.utils.BankConfig
import jakarta.transaction.Transactional
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
        val account = accountRepo.findByAccountNumber(accountNumber)
            ?: throw ResourceNotFoundException("Account number ${accountNumber} not found")

        val bankAcc = accountRepo.findByAccountNumber(BankConfig.SYSTEM_ACCOUNT_NUMBER.toLong())
            ?: throw ResourceNotFoundException("No System Account found?!")
        if(request.amount < BigDecimal.ZERO) throw BusinessRuleException("Amount should be positive")
        if(request.amount > BigDecimal.valueOf(10000)) throw BusinessRuleException("Cannot withdraw more than 10000.")
        if(account.balance < request.amount) throw BusinessRuleException("Insufficient balance")

        account.debit(request.amount)
        accountRepo.save(account)
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
        val account = accountRepo.findByAccountNumber(accountNumber)
            ?: throw ResourceNotFoundException("Account number ${accountNumber} not found")

        val bankAcc = accountRepo.findByAccountNumber(BankConfig.SYSTEM_ACCOUNT_NUMBER.toLong())
            ?: throw ResourceNotFoundException("No System Account found?!")
        if(request.amount < BigDecimal.ZERO) throw BusinessRuleException("Amount should be positive")
        if(request.amount < BigDecimal.valueOf(5)) throw BusinessRuleException("Must deposit at least 5.")
        if(request.amount > BigDecimal.valueOf(10000)) throw BusinessRuleException("Cannot deposit more than 10000.")

        account.credit(request.amount)
        accountRepo.save(account)
        return bankTransactionService.makeTransaction(
            senderAccount = account,
            receiverAccount = bankAcc,
            amount = request.amount,
            currency = Currency.valueOf(request.currency),
            type = TransactionType.DEPOSIT,
            status = TransactionStatus.SUCCESS,
        )
    }

    @Transactional
    override fun transfer(request: TransferRequest): TransactionResponse {
        val sender = userRepo.findByEmail(SecurityContextHolder.getContext().authentication?.name)
            ?: throw ResourceNotFoundException("User not found")
        val senderAcc = accountRepo.findByAccountNumberForUpdate(request.senderAccount)
            ?: throw ResourceNotFoundException("Sender account number not found")

        if (senderAcc.user?.id != sender.id) throw BusinessRuleException("Sender not found")

        val receiverAcc = accountRepo.findByAccountNumberForUpdate(request.receiverAccount)
            ?: throw ResourceNotFoundException("Receiver account number not found")

        if(request.amount <= BigDecimal.ZERO) throw BusinessRuleException("Amount must be positive")
        if(request.amount > BigDecimal.valueOf(10000)) throw BusinessRuleException("Cannot transfer more than 10000.")
        if(senderAcc.balance < request.amount) throw BusinessRuleException("Insufficient balance")
        if(senderAcc.id == receiverAcc.id) throw BusinessRuleException("Can't transfer to the same account")
        if(senderAcc.currency != receiverAcc.currency) throw BusinessRuleException("Currency mismatch")

        senderAcc.debit(request.amount)
        receiverAcc.credit(request.amount)

        accountRepo.saveAll(listOf(senderAcc, receiverAcc))

        return bankTransactionService.makeTransaction(
            senderAccount = senderAcc,
            receiverAccount = receiverAcc,
            amount = request.amount,
            currency = request.currency,
            type = TransactionType.TRANSFER,
            status = TransactionStatus.SUCCESS,
        )
    }
}