package firestorm.vuth.simplebank.service.impl

import firestorm.vuth.simplebank.dto.request.LoginRequest
import firestorm.vuth.simplebank.dto.request.RegisterRequest
import firestorm.vuth.simplebank.dto.response.ApiResponse
import firestorm.vuth.simplebank.dto.response.AuthResponse
import firestorm.vuth.simplebank.model.Account
import firestorm.vuth.simplebank.model.User
import firestorm.vuth.simplebank.repository.AccountRepo
import firestorm.vuth.simplebank.repository.UserRepo
import firestorm.vuth.simplebank.service.AuthService
import firestorm.vuth.simplebank.utils.JwtTokenService
import jakarta.transaction.Transactional
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepo: UserRepo,
    private val accountRepo: AccountRepo,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenService: JwtTokenService,
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: UserDetailsService
): AuthService {
    @Transactional
    override fun login(request: LoginRequest): AuthResponse {
        try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )

            val accessToken = jwtTokenService.generateAccessToken(authentication)
            val refreshToken = jwtTokenService.generateRefreshToken(authentication.name)
            return AuthResponse(accessToken, refreshToken)
        } catch (ex: BadCredentialsException) {
            throw RuntimeException("Invalid username or password")
        } catch (ex: Exception) {
            throw RuntimeException("Authentication error", ex)
        }
    }

    @Transactional
    override fun register(request: RegisterRequest): ApiResponse {
        if (userRepo.existsByEmail(request.email)) {
            throw BadCredentialsException("User with email ${request.email} already exists")
        }

        val user = User(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            password = passwordEncoder.encode(request.password),
        )

        val savedUser = userRepo.save(user)

        val account = Account(
            user = savedUser,
        )

        accountRepo.save(account)
        return ApiResponse(true, "Successfully registered successfully")
    }

    override fun refresh(token: String): AuthResponse? {
        val username = jwtTokenService.validateRefreshTokenAndGetUsername(token)
        ?: throw BadCredentialsException("Invalid refresh token")

        val user = userDetailsService.loadUserByUsername(username)
        val newAccessToken = jwtTokenService.generateAccessToken(
            UsernamePasswordAuthenticationToken(user, null, user.authorities)
        )
        val newRefreshToken = jwtTokenService.generateRefreshToken(username)

        return AuthResponse(newAccessToken, newRefreshToken)
    }
}