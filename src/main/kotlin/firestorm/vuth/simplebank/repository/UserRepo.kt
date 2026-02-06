package firestorm.vuth.simplebank.repository

import firestorm.vuth.simplebank.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepo: JpaRepository<User, UUID> {
    fun findByEmail(email: String?): User?
    fun existsByEmail(email: String): Boolean
}