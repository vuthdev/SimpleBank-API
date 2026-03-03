package firestorm.vuth.simplebank.controller

import firestorm.vuth.simplebank.dto.request.CustomerRequest
import firestorm.vuth.simplebank.dto.response.CustomerResponse
import firestorm.vuth.simplebank.model.Customer
import firestorm.vuth.simplebank.service.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CustomerController(
    val customerService: CustomerService
) {
    @GetMapping("/customers/me")
    fun getCustomerProfile(): ResponseEntity<CustomerResponse> =
        ResponseEntity.ok(customerService.getProfileDetails())

    @PutMapping("/customers/me")
    fun updateCustomer(@RequestBody request: CustomerRequest): ResponseEntity<CustomerResponse> =
        ResponseEntity.ok(customerService.updateProfile(request))
}