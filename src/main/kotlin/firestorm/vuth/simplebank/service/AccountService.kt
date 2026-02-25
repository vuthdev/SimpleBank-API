package firestorm.vuth.simplebank.service

import firestorm.vuth.simplebank.dto.request.AccountRequest
import firestorm.vuth.simplebank.dto.response.AccountDetailResponse
import java.util.UUID

interface AccountService {
    fun createAccount(request: AccountRequest): AccountDetailResponse
    fun deleteAccount(accountId: UUID)
    fun checkAccount(accountNumber: Long, userId: String): AccountDetailResponse
}