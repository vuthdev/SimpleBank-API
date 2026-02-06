package firestorm.vuth.simplebank.Mapper

import firestorm.vuth.simplebank.dto.response.AccountDetailResponse
import firestorm.vuth.simplebank.model.Account

fun Account.toResponse(): AccountDetailResponse = AccountDetailResponse(
    this.accountNumber,
    this.balance,
    this.currency,
)