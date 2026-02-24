package firestorm.vuth.simplebank.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.Exception

@Component
class RequestLoggingInterceptor: HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.setAttribute("startTime", System.currentTimeMillis())

        logger.info(
            "→ REQUEST  | method=${request.method} | path=${request.requestURI} | ip=${request.remoteAddr}"
        )

        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val duration = System.currentTimeMillis() - (request.getAttribute("startTime") as Long)

        val logMessage = "← RESPONSE | method=${request.method} | path=${request.requestURI} | status=${response.status} | duration=${duration}ms"

        if (duration > 1000) {
            logger.warn("🐢 SLOW $logMessage")
        } else {
            logger.info(logMessage)
        }

        if (ex != null) {
            logger.error("💥 EXCEPTION | path=${request.requestURI} | error=${ex.message}")
        }
    }
}