package firestorm.vuth.simplebank.mapper

import firestorm.vuth.simplebank.dto.response.CustomerResponse
import firestorm.vuth.simplebank.model.Customer

fun Customer.toResponse(): CustomerResponse =
    CustomerResponse (
        fullName = this.fullName,
        email = this.email,
        phoneNumber = this.phoneNumber,
        createdAt = this.createdAt
    )

fun List<Customer>.toResponse(): List<CustomerResponse> =
    this.mapNotNull { it.toResponse() }