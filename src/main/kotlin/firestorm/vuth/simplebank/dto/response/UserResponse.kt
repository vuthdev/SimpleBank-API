package firestorm.vuth.simplebank.dto.response

import java.util.UUID

data class UserResponse(
    val id: UUID?,
    val fullName: String?,
    val email: String?,
    val roles: List<String?>,
    val account: List<AccountDetailResponse>
)