package firestorm.vuth.simplebank.dto.request

import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class WithdrawRequest (
    @field:NotBlank
    val amount: BigDecimal,

    @field:NotBlank
    val currency: String,
)
