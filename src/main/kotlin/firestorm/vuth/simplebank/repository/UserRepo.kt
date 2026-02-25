package firestorm.vuth.simplebank.repository

import firestorm.vuth.simplebank.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepo: JpaRepository<User, UUID> {
    override fun findAll(pageable: Pageable): Page<User>
    fun findByEmail(email: String?): User?
    fun existsByEmail(email: String): Boolean
}