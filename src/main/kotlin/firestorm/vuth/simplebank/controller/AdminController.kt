package firestorm.vuth.simplebank.controller

import firestorm.vuth.simplebank.dto.request.DepositRequest
import firestorm.vuth.simplebank.dto.request.WithdrawRequest
import firestorm.vuth.simplebank.dto.response.AccountDetailResponse
import firestorm.vuth.simplebank.dto.response.TransactionResponse
import firestorm.vuth.simplebank.dto.response.UserResponse
import firestorm.vuth.simplebank.service.AccountService
import firestorm.vuth.simplebank.service.TransferService
import firestorm.vuth.simplebank.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
class AdminController(
    private val transferService: TransferService,
    private val accountService: AccountService,
    private val userService: UserService,
) {
    @PostMapping("/{accountNumber}/deposit")
    fun deposit(@PathVariable accountNumber: Long, @RequestBody request: DepositRequest): ResponseEntity<TransactionResponse> =
        ResponseEntity.ok(transferService.deposit(accountNumber, request))

    @PostMapping("/{accountNumber}/withdraw")
    fun withdraw(@PathVariable accountNumber: Long, @RequestBody request: WithdrawRequest): ResponseEntity<TransactionResponse> =
        ResponseEntity.ok(transferService.withdraw(accountNumber, request))

    @DeleteMapping("/account/delete/{accountNumber}")
    fun deleteAccount(@PathVariable accountNumber: Long): ResponseEntity<Unit> =
        ResponseEntity.ok(accountService.deleteAccount(accountNumber))

    @GetMapping("/user")
    fun getUser(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<List<UserResponse>> {
        val pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id")
        return ResponseEntity.ok(userService.getAllUsers(pageable))
    }

    @GetMapping("/user/{id}")
    fun getUserById(@PathVariable id: UUID): UserResponse {
        val user = userService.findById(id)
        return UserResponse(
            user?.id,
            user?.firstName + " " + user?.lastName,
            user?.username,
            user?.authorities?.map { it.authority } as List<String?>,
            user.bankAccounts.map { AccountDetailResponse(it.accountNumber, it.balance, it.currency) },
        )
    }
}