package firestorm.vuth.simplebank.dto.response

import firestorm.vuth.simplebank.model.Enum.UserRole
import java.util.UUID

data class UserResponse(
    val id: UUID?,
    val fullName: String?,
    val email: String?,
    val roles: List<UserRole>?,
    val account: List<AccountDetailResponse>?
)