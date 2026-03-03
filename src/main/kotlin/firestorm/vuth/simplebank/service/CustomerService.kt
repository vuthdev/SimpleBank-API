package firestorm.vuth.simplebank.service

import firestorm.vuth.simplebank.dto.request.CustomerRequest
import firestorm.vuth.simplebank.dto.response.CustomerResponse

interface CustomerService {
    fun getProfileDetails(): CustomerResponse
    fun updateProfile(request: CustomerRequest): CustomerResponse
}