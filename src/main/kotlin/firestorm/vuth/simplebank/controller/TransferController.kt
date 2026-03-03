package firestorm.vuth.simplebank.controller

import firestorm.vuth.simplebank.dto.request.TransferRequest
import firestorm.vuth.simplebank.dto.response.TransactionResponse
import firestorm.vuth.simplebank.service.BankTransactionService
import firestorm.vuth.simplebank.service.TransferService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class TransferController(
    private val transferService: TransferService,
    private val bankTransactionService: BankTransactionService
) {
    @PostMapping("/transactions/transfer")
    fun transfer(@RequestBody request: TransferRequest): ResponseEntity<TransactionResponse> {
        return ResponseEntity.ok(transferService.transfer(request))
    }

    @GetMapping("/transactions")
    fun getUserTransaction(pageable: Pageable): ResponseEntity<List<TransactionResponse>> {
        return ResponseEntity.ok(bankTransactionService.getTransaction(pageable))
    }
}