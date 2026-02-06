package firestorm.vuth.simplebank.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:NotBlank("First name is required")
    val firstName: String,

    @field:NotBlank("Last name is required")
    val lastName: String,

    @field:NotBlank("Email is required")
    @field:Email
    val email: String,

    @field:NotBlank
    val password: String
)