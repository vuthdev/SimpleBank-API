package firestorm.vuth.simplebank.dto.request

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class AccountRequest(
    @field:NotBlank
    var userId: UUID,

    @field:NotBlank
    var currency: String = "USD",
)