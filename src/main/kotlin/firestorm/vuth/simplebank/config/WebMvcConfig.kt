package firestorm.vuth.simplebank.config

import firestorm.vuth.simplebank.interceptor.AuditTrailInterceptor
import firestorm.vuth.simplebank.interceptor.RateLimitInterceptor
import firestorm.vuth.simplebank.interceptor.RequestLoggingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val requestLoggingInterceptor: RequestLoggingInterceptor,
    private val rateLimitInterceptor: RateLimitInterceptor,
    private val auditTrailInterceptor: AuditTrailInterceptor
): WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(requestLoggingInterceptor)
            .addPathPatterns("/api/**")

        registry.addInterceptor(rateLimitInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns(
                "/api/auth/login",
                "/api/auth/register",
            )

        registry.addInterceptor(auditTrailInterceptor)
            .addPathPatterns(
                "/api/transactions/transfer",
                "/api/user/account"
            )
    }
}