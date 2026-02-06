package firestorm.vuth.simplebank.service

import firestorm.vuth.simplebank.dto.request.LoginRequest
import firestorm.vuth.simplebank.dto.request.RegisterRequest
import firestorm.vuth.simplebank.dto.response.ApiResponse
import firestorm.vuth.simplebank.dto.response.AuthResponse

interface AuthService {
    fun login(request: LoginRequest): AuthResponse
    fun register(request: RegisterRequest): ApiResponse
    fun refresh(token: String): AuthResponse?
}