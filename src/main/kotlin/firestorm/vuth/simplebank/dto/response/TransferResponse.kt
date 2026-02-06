package firestorm.vuth.simplebank.dto.response

import firestorm.vuth.simplebank.model.Enum.Currency
import firestorm.vuth.simplebank.model.Enum.TransactionStatus
import firestorm.vuth.simplebank.model.Enum.TransactionType
import java.math.BigDecimal
import java.util.UUID

data class TransferResponse(
    val id: UUID,
    val senderAccount: String,
    val receiverAccount: String,
    val amount: BigDecimal,
    val currency: Currency,
    val type: TransactionType,
    val status: TransactionStatus,
)
