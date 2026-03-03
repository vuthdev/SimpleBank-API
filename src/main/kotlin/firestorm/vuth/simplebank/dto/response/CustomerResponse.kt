package firestorm.vuth.simplebank.dto.response

import java.time.LocalDateTime

data class CustomerResponse(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val createdAt: LocalDateTime
)
