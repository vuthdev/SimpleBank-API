package firestorm.vuth.simplebank.utils

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityUtils {
    fun getCurrentUsername(): String? {
        val auth = SecurityContextHolder.getContext().authentication
        return auth?.name
    }
}