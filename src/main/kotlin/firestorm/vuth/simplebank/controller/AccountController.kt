package firestorm.vuth.simplebank.controller

import firestorm.vuth.simplebank.dto.request.AccountRequest
import firestorm.vuth.simplebank.dto.response.AccountDetailResponse
import firestorm.vuth.simplebank.dto.response.UserResponse
import firestorm.vuth.simplebank.service.AccountService
import firestorm.vuth.simplebank.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/account")
class AccountController(
    private val accountService: AccountService,
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal jwt: Jwt): ResponseEntity<UserResponse> =
        ResponseEntity.ok(userService.currentUser(jwt.subject))

    @GetMapping("/{accountNumber}")
    fun checkAccount(@PathVariable accountNumber: Long, @AuthenticationPrincipal user: Jwt): ResponseEntity<AccountDetailResponse> =
        ResponseEntity.ok(accountService.checkAccount(accountNumber, user.subject))

    @PostMapping
    fun createAccount(@RequestBody request: AccountRequest): ResponseEntity<AccountDetailResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request))
}