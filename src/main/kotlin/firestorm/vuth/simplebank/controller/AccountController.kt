package firestorm.vuth.simplebank.controller

import firestorm.vuth.simplebank.dto.request.AccountRequest
import firestorm.vuth.simplebank.dto.response.AccountDetailResponse
import firestorm.vuth.simplebank.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AccountController(
    private val accountService: AccountService,
) {
    @GetMapping("/accounts")
    fun getAccounts(): ResponseEntity<List<AccountDetailResponse>> =
        ResponseEntity.ok(accountService.listAllAccounts())

    @GetMapping("/accounts/{accountNumber}")
    fun checkAccount(@PathVariable accountNumber: Long): ResponseEntity<AccountDetailResponse> =
        ResponseEntity.ok(accountService.checkAccount(accountNumber))

    @PostMapping("/accounts")
    fun createAccount(@RequestBody request: AccountRequest): ResponseEntity<AccountDetailResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request))
}