package firestorm.vuth.simplebank.model

import jakarta.persistence.Column
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

    @Column(name = "user_email", nullable = false)
    val userEmail: String? = null,
    val action: String? = null,
    val path: String? = null,
    val ip: String? = null,
    val status: Int? = null,

    @Column(name = "duration_ms", nullable = false)
    val durationMs: Long? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)