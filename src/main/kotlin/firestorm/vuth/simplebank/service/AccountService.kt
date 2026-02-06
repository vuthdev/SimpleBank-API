package firestorm.vuth.simplebank.service

import firestorm.vuth.simplebank.dto.request.AccountRequest
import firestorm.vuth.simplebank.dto.response.AccountDetailResponse

interface AccountService {
    fun createAccount(request: AccountRequest): AccountDetailResponse
    fun deleteAccount(accountNumber: Long, email: String)
    fun checkAccount(accountNumber: Long, email: String): AccountDetailResponse
}