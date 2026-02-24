package firestorm.vuth.simplebank.repository

import firestorm.vuth.simplebank.model.AuditLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AuditLogRepository: JpaRepository<AuditLog, UUID> {
    fun findByUserEmailOrderByCreatedAtDesc(userEmail: String): List<AuditLog>
    fun findByStatusGreaterThanEqualOrderByCreatedAtDesc(status: Int): List<AuditLog>
}