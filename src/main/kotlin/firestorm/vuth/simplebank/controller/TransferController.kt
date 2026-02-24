package firestorm.vuth.simplebank.controller

import firestorm.vuth.simplebank.dto.request.TransferRequest
import firestorm.vuth.simplebank.dto.response.TransactionResponse
import firestorm.vuth.simplebank.service.BankTransactionService
import firestorm.vuth.simplebank.service.TransferService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/transactions")
class TransferController(
    private val transferService: TransferService,
    private val bankTransactionService: BankTransactionService
) {
    @PostMapping("/transfer")
    fun transfer(@RequestBody request: TransferRequest): ResponseEntity<TransactionResponse> {
        return ResponseEntity.ok(transferService.transfer(request))
    }

    @GetMapping
    fun getUserTransaction(@AuthenticationPrincipal jwt: Jwt, pageable: Pageable): ResponseEntity<List<TransactionResponse>> {
        return ResponseEntity.ok(bankTransactionService.getTransaction(jwt.subject, pageable))
    }
}