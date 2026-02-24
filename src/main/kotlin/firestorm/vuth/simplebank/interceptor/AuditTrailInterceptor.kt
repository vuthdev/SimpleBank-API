package firestorm.vuth.simplebank.interceptor

import firestorm.vuth.simplebank.service.AuditLogService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.Exception
import java.util.UUID

@Component
class AuditTrailInterceptor(
    private val auditLongService: AuditLogService,
): HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any?): Boolean {
        request.setAttribute("auditStartTime", System.currentTimeMillis())
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: Exception?
    ) {
        val duration = System.currentTimeMillis() - request.getAttribute("auditStartTime") as Long
        val userId = getAuthenticatedUserId()

        auditLongService.save(
            userId = userId,
            action = request.method,
            path = request.requestURI,
            ip = request.remoteAddr,
            status = response.status,
            durationMs = duration,
        )
    }

    fun getAuthenticatedUserId(): UUID? {
        val auth = SecurityContextHolder.getContext().authentication
        val jwt = auth?.credentials as? Jwt ?: return null
        return runCatching { UUID.fromString(jwt.subject) }.getOrNull()
    }
}