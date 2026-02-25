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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select id, account_number, balance, currency, user_id, created_at from accounts where account_number = :accountNumber", nativeQuery = true)
    fun findByAccountNumberForUpdate(@Param("accountNumber") accNum: Long): Account?

    @Query("select COUNT(*) from Account a JOIN User u ON a.user.id = u.id WHERE u.email = :email")
    fun countByAccountByUserId(@Param("email") email: String): Long

    @Modifying
    @Query("UPDATE accounts SET balance = balance - :amount WHERE account_number = :accountNumber AND balance >= :amount AND user_id = :userId", nativeQuery = true)
    fun debitBalanceForTransfer(
        @Param("accountNumber") accountNumber: Long, @Param("amount") amount: BigDecimal, @Param("userId") userId: UUID?
    ): Int

    @Modifying
    @Query("UPDATE accounts SET balance = balance - :amount WHERE account_number = :accountNumber AND balance >= :amount", nativeQuery = true)
    fun debitBalance(
        @Param("accountNumber") accountNumber: Long, @Param("amount") amount: BigDecimal
    ): Int

    @Modifying
    @Query("UPDATE accounts SET balance = balance + :amount WHERE account_number = :accountNumber", nativeQuery = true)
    fun creditBalance(@Param("accountNumber") accountNumber: Long, @Param("amount") amount: BigDecimal): Int
}