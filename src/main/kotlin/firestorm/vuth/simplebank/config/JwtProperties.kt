package firestorm.vuth.simplebank.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String,
    val accessTokenExpireMinutes: Long = 15,
    val refreshTokenExpireMinutes: Long = 7,
    val issuer: String = "SimpleBank",
)