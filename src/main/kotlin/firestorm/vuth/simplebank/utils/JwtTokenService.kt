package firestorm.vuth.simplebank.utils

import firestorm.vuth.simplebank.config.JwtProperties
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey
import kotlin.io.encoding.Base64
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@Component
class JwtTokenService(
    private val properties: JwtProperties
) {
    private val secret: SecretKey = Keys.hmacShaKeyFor(
        Base64.decode(properties.secret)
    )
    private val parser: JwtParser = Jwts.parser()
        .verifyWith(secret)
        .build()

    fun generateAccessToken(authentication: Authentication): String {
        val user = authentication.principal as UserDetails
        val now = Date()
        val expiry = Date(now.time + properties.accessTokenExpireMinutes.minutes.toJavaDuration().toMillis())

        return Jwts.builder()
            .subject(user.username) // store email as username because login with email is cool
            .claim("roles", user.authorities.map { it.authority?.removePrefix("ROLE_") })
            .issuedAt(Date())
            .expiration(expiry)
            .issuer(properties.issuer)
            .id(UUID.randomUUID().toString())
            .signWith(secret)
            .compact()
    }

    fun generateRefreshToken(username: String): String {
        val now = Date()
        val expiry = Date(now.time + properties.refreshTokenExpireMinutes.days.toJavaDuration().toMillis())

        return Jwts.builder()
            .subject(username)
            .claim("type", "refresh")
            .issuedAt(now)
            .expiration(expiry)
            .issuer(properties.issuer)
            .id(UUID.randomUUID().toString())
            .signWith(secret)
            .compact()
    }

    fun validateRefreshTokenAndGetUsername(authToken: String): String? {
        return try {
            val claims = parser.parseSignedClaims(authToken).payload
            if (claims["type"] != "refresh") return null
            claims.subject
        }catch (e: Exception) {
            null
        }
    }

    val secretKey: SecretKey get() = secret
}