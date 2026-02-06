package firestorm.vuth.simplebank.Mapper

import firestorm.vuth.simplebank.dto.response.TransactionResponse
import firestorm.vuth.simplebank.model.Transaction

fun Transaction.toResponse(): TransactionResponse = TransactionResponse(
    this.id!!,
    this.senderAccount?.accountNumber,
    this.receiverAccount?.accountNumber,
    this.currency,
    this.amount,
    this.type,
    this.status,
    this.createdAt
)