package firestorm.vuth.simplebank.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.servlet.HandlerInterceptor
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class RateLimitInterceptor: HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val MAX_REQUESTS = 5
    private val WINDOW_MS = 60_000L

    private val requestCounts = ConcurrentHashMap<String, RequestRecord>()

    data class RequestRecord(
        val count: AtomicInteger = AtomicInteger(0),
        val windowStart: Long = System.currentTimeMillis(),
    )

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any?): Boolean {
        val userId = getAuthenticatedUserId() ?: run {
            return true
        }

        val now = System.currentTimeMillis()
        val record = requestCounts.compute(userId) { _, existing ->
            when {
                existing == null -> RequestRecord()
                now - existing.windowStart > WINDOW_MS -> RequestRecord()
                else -> existing
            }
        }!!

        val currentCount = record.count.incrementAndGet()

        response.setHeader("X-RateLimit-Limit", MAX_REQUESTS.toString())
        response.setHeader("X-RateLimit-Remaining", maxOf(0, MAX_REQUESTS - currentCount).toString())
        response.setHeader("X-RateLimit-Reset", ((record.windowStart + WINDOW_MS) / 1000).toString())

        return if (currentCount > MAX_REQUESTS) {
            logger.warn("🚫 RATE LIMIT exceeded | user=$userId | count=$currentCount | path=${request.requestURI}")
            response.status = HttpStatus.TOO_MANY_REQUESTS.value()
            response.writer.write("""{"error": "Too many requests. Please wait before retrying."}""")
            false
        } else {
            logger.debug("✅ RATE LIMIT ok | user=$userId | count=$currentCount/$MAX_REQUESTS | path=${request.requestURI}")
            true
        }
    }

    fun getAuthenticatedUserId(): String? {
        val auth = SecurityContextHolder.getContext().authentication ?: return null
        val jwt = auth.principal as? Jwt ?: return null
        return jwt.subject
    }
}