package firestorm.vuth.simplebank.controller

import firestorm.vuth.simplebank.model.Account
import firestorm.vuth.simplebank.model.Enum.UserRole
import firestorm.vuth.simplebank.model.User
import firestorm.vuth.simplebank.repository.AccountRepo
import firestorm.vuth.simplebank.repository.UserRepo
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminInitializer(
    private val userRepository: UserRepo,
    private val accountRepo: AccountRepo,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (!userRepository.existsByEmail("admin@bank.com")) {

            val admin = User(
                email = "admin@bank.com",
                firstName = "System",
                lastName = "Admin",
                password = passwordEncoder.encode("admin123"),
                roles = mutableSetOf(UserRole.USER, UserRole.ADMIN)
            )

            userRepository.save(admin)

            val bankAccount = Account(
                user = admin
            )
            accountRepo.save(bankAccount)
            println("✅ Default admin created")
        }
    }
}