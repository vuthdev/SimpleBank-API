package firestorm.vuth.simplebank.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:NotBlank("username is required")
    @field:Min(3, "username must be at least 3 characters")
    val username: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    val fullName: String,

    @field:NotBlank
    val phoneNumber: String,

    @field:NotBlank
    @field:Email
    val email: String,
)