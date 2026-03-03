package firestorm.vuth.simplebank.repository

import firestorm.vuth.simplebank.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface AccountRepo: JpaRepository<Account, UUID> {
    @Query(value = "SELECT * FROM Account a ORDER BY a.accountNumber DESC LIMIT 1", nativeQuery = true)
    fun findLastAccount(): Account?

    fun findByAccountNumber(accountNumber: Long): Account?
}