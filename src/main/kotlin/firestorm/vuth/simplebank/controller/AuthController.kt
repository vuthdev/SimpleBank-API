package firestorm.vuth.simplebank.controller

import firestorm.vuth.simplebank.dto.request.LoginRequest
import firestorm.vuth.simplebank.dto.request.RefreshTokenRequest
import firestorm.vuth.simplebank.dto.request.RegisterRequest
import firestorm.vuth.simplebank.dto.response.ApiResponse
import firestorm.vuth.simplebank.dto.response.AuthResponse
import firestorm.vuth.simplebank.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val success = authService.login(request)
        return ResponseEntity.status(HttpStatus.OK).body(success)
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<ApiResponse> {
        val register = authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(register)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody refreshToken: RefreshTokenRequest): ResponseEntity<Any> {
        val token = authService.refresh(refreshToken.refreshToken) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            "invalid refresh token"
        )

        return ResponseEntity.status(HttpStatus.OK).body(token)
    }
}