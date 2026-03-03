package firestorm.vuth.simplebank.repository

import firestorm.vuth.simplebank.model.AuditLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AuditLogRepo: JpaRepository<AuditLog, UUID> {
    fun findByUsernameOrderByCreatedAtDesc(username: String): List<AuditLog>
    fun findByStatusGreaterThanEqualOrderByCreatedAtDesc(status: Int): List<AuditLog>
}