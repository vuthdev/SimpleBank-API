package firestorm.vuth.simplebank.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "audit_logs")
class AuditLog (
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val userId: UUID? = null,
    val action: String? = null,
    val path: String? = null,
    val ip: String? = null,
    val status: Int? = null,
    val durationMs: Long? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)