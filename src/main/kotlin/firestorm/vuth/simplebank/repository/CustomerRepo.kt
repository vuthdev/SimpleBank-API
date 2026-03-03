package firestorm.vuth.simplebank.repository

import firestorm.vuth.simplebank.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerRepo: JpaRepository<Customer, UUID>