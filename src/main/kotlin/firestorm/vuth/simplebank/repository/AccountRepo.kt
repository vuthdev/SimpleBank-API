package firestorm.vuth.simplebank.repository

import firestorm.vuth.simplebank.model.Account
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigDecimal
import java.util.UUID

interface AccountRepo: JpaRepository<Account, UUID> {
    @Query(value = "SELECT * FROM Account a ORDER BY a.accountNumber DESC LIMIT 1", nativeQuery = true)
    fun findLastAccount(): Account?

    fun findByAccountNumber(accountNumber: Long): Account?

    @Query("select COUNT(*) from Account a JOIN User u ON a.user.id = u.id WHERE u.email = :email")
    fun countByAccountByUserId(@Param("email") email: String): Long
}