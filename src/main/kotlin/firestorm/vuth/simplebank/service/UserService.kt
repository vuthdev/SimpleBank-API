package firestorm.vuth.simplebank.service

import firestorm.vuth.simplebank.dto.response.UserResponse
import firestorm.vuth.simplebank.model.User
import org.springframework.data.domain.Pageable
import java.util.UUID

interface UserService {
    fun getAllUsers(pageable: Pageable): List<UserResponse>
    fun currentUser(): UserResponse
    fun findById(id: UUID): User?
    fun deleteById(id: UUID)
}