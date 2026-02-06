package firestorm.vuth.simplebank.service.impl

import firestorm.vuth.simplebank.Mapper.toResponse
import firestorm.vuth.simplebank.dto.request.AccountRequest
import firestorm.vuth.simplebank.dto.response.AccountDetailResponse
import firestorm.vuth.simplebank.exception.BusinessRuleException
import firestorm.vuth.simplebank.exception.ResourceNotFoundException
import firestorm.vuth.simplebank.model.Account
import firestorm.vuth.simplebank.model.Enum.Currency
import firestorm.vuth.simplebank.repository.AccountRepo
import firestorm.vuth.simplebank.repository.UserRepo
import firestorm.vuth.simplebank.service.AccountService
import firestorm.vuth.simplebank.utils.BankConfig
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl(
    private val accountRepo: AccountRepo,
    private val userRepo: UserRepo,
): AccountService {
    override fun createAccount(request: AccountRequest): AccountDetailResponse {
        val user = userRepo.findById(request.userId).orElseThrow { ResourceNotFoundException("User does not exist") }

        if (user.bankAccounts.count() <= BankConfig.MIN_ACCOUNT) throw BusinessRuleException("Account does not belong to user")

        val account = Account(
            user = user,
            currency = Currency.valueOf(request.currency),
        )

        return accountRepo.save(account).toResponse()
    }

    override fun deleteAccount(
        accountNumber: Long,
        email: String
    ) {
        val user = userRepo.findByEmail(email)
            ?: throw ResourceNotFoundException("User does not exist")
        val account = accountRepo.findByAccountNumberForUpdate(accountNumber)
            ?: throw ResourceNotFoundException("Account does not exist")

        if (account.user?.id != user.id) throw BusinessRuleException("Account does not belong to user")

        return accountRepo.deleteById(account.id!!)
    }

    override fun checkAccount(
        accountNumber: Long,
        email: String
    ): AccountDetailResponse {
        val user = userRepo.findByEmail(email)
            ?: throw ResourceNotFoundException("User does not exist")
        val account = accountRepo.findByAccountNumber(accountNumber)
            ?: throw ResourceNotFoundException("Account does not exist")

        if (account.user?.id == user.id) {
            return AccountDetailResponse(
                account.accountNumber,
                account.balance,
                account.currency,
            )
        } else {
            throw BusinessRuleException("You are not authorized to operate on this account")
        }
    }
}