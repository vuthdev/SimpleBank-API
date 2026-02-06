package firestorm.vuth.simplebank.dto.response

import firestorm.vuth.simplebank.model.Enum.Currency
import firestorm.vuth.simplebank.model.Enum.TransactionStatus
import firestorm.vuth.simplebank.model.Enum.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class TransactionResponse(
    val id: UUID?,
    var senderNumber: Long?,
    var receiverNumber: Long?,
    var currency: Currency,
    var amount: BigDecimal,
    var type: TransactionType?,
    var status: TransactionStatus?,
    var createdAt: LocalDateTime,
)