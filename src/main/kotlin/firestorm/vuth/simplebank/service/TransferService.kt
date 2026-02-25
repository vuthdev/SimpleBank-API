package firestorm.vuth.simplebank.service

import firestorm.vuth.simplebank.dto.request.DepositRequest
import firestorm.vuth.simplebank.dto.request.TransferRequest
import firestorm.vuth.simplebank.dto.request.WithdrawRequest
import firestorm.vuth.simplebank.dto.response.TransactionResponse

interface TransferService {
    fun withdraw(accountNumber: Long, request: WithdrawRequest): TransactionResponse
    fun deposit(accountNumber: Long, request: DepositRequest): TransactionResponse
    fun transfer(userId: String, request: TransferRequest): TransactionResponse
}