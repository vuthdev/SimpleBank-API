package firestorm.vuth.simplebank.dto.response

import firestorm.vuth.simplebank.model.Enum.Currency
import java.math.BigDecimal

data class AccountDetailResponse(
    val accountNumber: Long?,
    val balance: BigDecimal,
    val currency: Currency,
)