package firestorm.vuth.simplebank.service.impl

import firestorm.vuth.simplebank.dto.response.UserResponse
import firestorm.vuth.simplebank.mapper.toResponse
import firestorm.vuth.simplebank.model.CustomUserDetails
import firestorm.vuth.simplebank.model.User
import firestorm.vuth.simplebank.repository.UserRepo
import firestorm.vuth.simplebank.service.UserService
import firestorm.vuth.simplebank.utils.SecurityUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserServiceImpl(
    private val userRepo: UserRepo
): UserService, UserDetailsService {
    override fun getAllUsers(pageable: Pageable): List<UserResponse> {
        val users: Page<User> = userRepo.findAll(pageable)
        return users.content.toResponse()
    }

    override fun findById(id: UUID): User {
        return userRepo.findById(id).orElseThrow { UsernameNotFoundException("User with id $id not found") }
    }

    override fun deleteById(id: UUID) {
        userRepo.deleteById(id)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepo.findByUsername(username)
            ?: throw UsernameNotFoundException("User with email $username not found")
        return CustomUserDetails(
            id = user.id,
            username = user.username,
            password = user.getPassword(),
            authorities = user.roles.map { SimpleGrantedAuthority("ROLE_${it}") },
        )
    }
}