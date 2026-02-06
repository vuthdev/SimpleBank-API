package firestorm.vuth.simplebank.utils

import firestorm.vuth.simplebank.model.Account
import java.math.BigDecimal

fun Account.credit(amount: BigDecimal) {
    this.balance = this.balance.plus(amount)
}

fun Account.debit(amount: BigDecimal) {
    this.balance = this.balance.minus(amount)
}