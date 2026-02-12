package firestorm.vuth.simplebank.mapper

import firestorm.vuth.simplebank.dto.response.TransactionResponse
import firestorm.vuth.simplebank.model.Transaction

fun Transaction.toResponse(): TransactionResponse {
    val txId = checkNotNull(this.id) {
        "Transaction must be persisted before converting to response"
    }

    return TransactionResponse(
        id = txId,
        senderNumber = this.senderAccount?.accountNumber,
        receiverNumber = this.receiverAccount?.accountNumber,
        currency = this.currency,
        amount = this.amount,
        type = this.type,
        status = this.status,
        createdAt = this.createdAt
    )
}

fun List<Transaction>.toResponse(): List<TransactionResponse> =
    mapNotNull { it.toResponseOrNull() }

fun Transaction.toResponseOrNull(): TransactionResponse? {
    val txId = this.id ?: return null

    return TransactionResponse(
        id = txId,
        senderNumber = this.senderAccount?.accountNumber,
        receiverNumber = this.receiverAccount?.accountNumber,
        currency = this.currency,
        amount = this.amount,
        type = this.type,
        status = this.status,
        createdAt = this.createdAt
    )
}