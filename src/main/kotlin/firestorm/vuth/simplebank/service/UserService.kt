package firestorm.vuth.simplebank.service

import firestorm.vuth.simplebank.dto.response.UserResponse
import firestorm.vuth.simplebank.model.User
import org.springframework.security.oauth2.jwt.Jwt
import java.util.UUID

interface UserService {
    fun currentUser(jwt: Jwt): UserResponse
    fun findById(id: UUID): User?
    fun deleteById(id: UUID)
}