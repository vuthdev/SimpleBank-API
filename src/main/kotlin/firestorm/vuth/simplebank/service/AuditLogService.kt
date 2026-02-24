package firestorm.vuth.simplebank.service

import firestorm.vuth.simplebank.model.AuditLog
import firestorm.vuth.simplebank.repository.AuditLogRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuditLogService(
    private val auditLogRepository: AuditLogRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Async
    fun save(
        userId: UUID?,
        action: String,
        path: String,
        ip: String,
        status: Int,
        durationMs: Long,
    ) {
        try {
            auditLogRepository.save(
                AuditLog(
                    userId = userId,
                    action = action,
                    path = path,
                    ip = ip,
                    status = status,
                    durationMs = durationMs,
                )
            )
        } catch (ex: Exception) {
            logger.error("Failed to save audit log | path=${path} | error=${ex.message}")
        }
    }
}