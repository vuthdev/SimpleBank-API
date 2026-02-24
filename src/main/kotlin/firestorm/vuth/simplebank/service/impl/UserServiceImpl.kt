package firestorm.vuth.simplebank.service.impl

import firestorm.vuth.simplebank.dto.response.UserResponse
import firestorm.vuth.simplebank.mapper.toResponse
import firestorm.vuth.simplebank.model.User
import firestorm.vuth.simplebank.repository.UserRepo
import firestorm.vuth.simplebank.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserServiceImpl(
    private val userRepo: UserRepo,
): UserService, UserDetailsService {
    override fun getAllUsers(pageable: Pageable): List<UserResponse> {
        val users: Page<User> = userRepo.findAll(pageable)
        return users.content.toResponse()
    }

    override fun currentUser(jwt: Jwt): UserResponse {
        val userEmail = jwt.subject
        val user = userRepo.findByEmail(userEmail)
            ?: throw UsernameNotFoundException("User not found")

        return user.toResponse()
    }

    override fun findById(id: UUID): User {
        return userRepo.findById(id).orElseThrow { UsernameNotFoundException("User with id $id not found") }
    }

    override fun deleteById(id: UUID) {
        userRepo.deleteById(id)
    }

    override fun loadUserByUsername(email: String): UserDetails =
        userRepo.findByEmail(email)
            ?: throw UsernameNotFoundException("User with email $email not found")
}