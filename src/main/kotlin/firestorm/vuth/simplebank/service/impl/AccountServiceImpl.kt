package firestorm.vuth.simplebank.service.impl

import firestorm.vuth.simplebank.mapper.toResponse
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
import firestorm.vuth.simplebank.utils.SecurityUtils
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AccountServiceImpl(
    private val accountRepo: AccountRepo,
    private val userRepo: UserRepo,
    private val securityUtils: SecurityUtils
): AccountService {
    override fun createAccount(request: AccountRequest): AccountDetailResponse {
        val username = securityUtils.getCurrentUsername()
        val user = userRepo.findByUsername(username)
            ?: throw ResourceNotFoundException("User does not exist")

        val customer = user.customer ?: throw ResourceNotFoundException("Customer profile not found")
        if (customer.bankAccounts.size >= BankConfig.MAX_ACCOUNT_PER_USER) throw BusinessRuleException("You cannot create more than ${BankConfig.MAX_ACCOUNT_PER_USER} accounts!")

        val account = Account(
            customer = customer,
            currency = Currency.valueOf(request.currency),
        )

        return accountRepo.save(account).toResponse()
    }

    override fun deleteAccount(accountId: UUID) =
        accountRepo.deleteById(accountId)

    override fun checkAccount(
        accountNumber: Long
    ): AccountDetailResponse {
        val username = securityUtils.getCurrentUsername()
        val user = userRepo.findByUsername(username)
            ?: throw ResourceNotFoundException("User does not exist")
        val account = accountRepo.findByAccountNumber(accountNumber)
            ?: throw ResourceNotFoundException("Account does not exist")

        if (account.customer?.id == user.customer?.id) {
            return AccountDetailResponse(
                account.accountNumber,
                account.balance,
                account.currency,
            )
        } else {
            throw BusinessRuleException("You are not authorized to operate on this account")
        }
    }

    override fun listAllAccounts(): List<AccountDetailResponse> {
        val username = securityUtils.getCurrentUsername()
        val user = userRepo.findByUsername(username)
            ?: throw ResourceNotFoundException("User does not exist")

        val customer = user.customer ?: throw ResourceNotFoundException("Customer profile not found")

        val accounts = customer.bankAccounts
        if (accounts.isEmpty()) return emptyList()

        return accounts.toResponse()
    }
}