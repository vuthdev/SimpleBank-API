package firestorm.vuth.simplebank.dto.request

import firestorm.vuth.simplebank.model.Enum.Currency
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class TransferRequest (
    @field:NotBlank
    var senderAccount: Long,
    @field:NotBlank
    var receiverAccount: Long,
    @field:NotBlank
    @field:Positive
    var amount: BigDecimal,
    @field:NotBlank
    var currency: Currency,
)