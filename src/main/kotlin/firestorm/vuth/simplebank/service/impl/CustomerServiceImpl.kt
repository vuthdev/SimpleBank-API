package firestorm.vuth.simplebank.service.impl

import firestorm.vuth.simplebank.dto.request.CustomerRequest
import firestorm.vuth.simplebank.dto.response.CustomerResponse
import firestorm.vuth.simplebank.exception.ResourceNotFoundException
import firestorm.vuth.simplebank.mapper.toResponse
import firestorm.vuth.simplebank.model.Customer
import firestorm.vuth.simplebank.repository.CustomerRepo
import firestorm.vuth.simplebank.repository.UserRepo
import firestorm.vuth.simplebank.service.CustomerService
import firestorm.vuth.simplebank.utils.SecurityUtils
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    private val customerRepo: CustomerRepo,
    private val userRepo: UserRepo,
    private val securityUtils: SecurityUtils
): CustomerService {
    fun getCurrentCustomer(): Customer {
        val username = securityUtils.getCurrentUsername()
        val user = userRepo.findByUsername(username)
            ?: throw ResourceNotFoundException("User $username not found!")
        val customer = user.customer ?: throw ResourceNotFoundException("Customer profile not found")
        return customer
    }

    override fun getProfileDetails(): CustomerResponse =
        getCurrentCustomer().toResponse()

    override fun updateProfile(request: CustomerRequest): CustomerResponse {
        val customer = getCurrentCustomer()

        request.fullName.let { customer.fullName = it }
        request.email.let { customer.email = it }
        request.phoneNumber.let { customer.phoneNumber = it }

        return customerRepo.save(customer).toResponse()
    }
}