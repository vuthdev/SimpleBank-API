package firestorm.vuth.simplebank.mapper

import firestorm.vuth.simplebank.dto.response.AccountDetailResponse
import firestorm.vuth.simplebank.dto.response.UserResponse
import firestorm.vuth.simplebank.model.User

fun User.toResponse(): UserResponse {
    val txId = checkNotNull(this.id) { "Id cannot be null" }

    return UserResponse(
        id = txId,
        fullName = this.firstName + " " + this.lastName,
        email = this.email,
        roles = this.roles.toList(),
        account = this.bankAccounts.map { AccountDetailResponse(it.accountNumber, it.balance, it.currency) },
    )
}

fun List<User>.toResponse(): List<UserResponse> =
    this.mapNotNull { it.toResponse() }