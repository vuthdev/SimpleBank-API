package firestorm.vuth.simplebank.mapper

import firestorm.vuth.simplebank.dto.response.UserResponse
import firestorm.vuth.simplebank.model.User

fun User.toResponse(): UserResponse {
    val txId = checkNotNull(id) { "Id cannot be null" }

    return UserResponse(
        id = txId,
        username = this.username,
        roles = this.roles.toList(),
    )
}

fun List<User>.toResponse(): List<UserResponse> =
    this.mapNotNull { it.toResponse() }