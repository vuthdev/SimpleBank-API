package firestorm.vuth.simplebank.controller

import firestorm.vuth.simplebank.model.Account
import firestorm.vuth.simplebank.model.Customer
import firestorm.vuth.simplebank.model.Enum.UserRole
import firestorm.vuth.simplebank.model.User
import firestorm.vuth.simplebank.repository.AccountRepo
import firestorm.vuth.simplebank.repository.CustomerRepo
import firestorm.vuth.simplebank.repository.UserRepo
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminInitializer(
    private val userRepository: UserRepo,
    private val customerRepo: CustomerRepo,
    private val accountRepo: AccountRepo,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {
    override fun run(vararg args: String) {
        if (!userRepository.existsByUsername("system")) {

            val user = User(
                username = "system",
                password = passwordEncoder.encode("admin123"),
                roles = mutableSetOf(UserRole.USER, UserRole.ADMIN)
            )
            val savedUser = userRepository.save(user)

            val customer = Customer(
                fullName = "system",
                email = "admin@bank.com",
                phoneNumber = "0712929468",
                user = savedUser,
            )
            val savedCustomer = customerRepo.save(customer)

            val bankAccount = Account(
                customer = savedCustomer,
            )
            accountRepo.save(bankAccount)
            println("✅ Default admin created")
        }
    }
}