package firestorm.vuth.simplebank.utils

import firestorm.vuth.simplebank.config.JwtProperties
import firestorm.vuth.simplebank.model.CustomUserDetails
import firestorm.vuth.simplebank.model.User
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
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
        val user = authentication.principal as CustomUserDetails
        val now = Date()
        val expiry = Date(now.time + properties.accessTokenExpireMinutes.minutes.toJavaDuration().toMillis())

        return Jwts.builder()
            .subject(user.id.toString())
            .claim("roles", user.authorities.mapNotNull { it.authority?.removePrefix("ROLE_") })
            .claim("type", "access")
            .issuedAt(Date())
            .expiration(expiry)
            .issuer(properties.issuer)
            .id(UUID.randomUUID().toString())
            .signWith(secret)
            .compact()
    }

    fun generateRefreshToken(userId: String): String {
        val now = Date()
        val expiry = Date(now.time + properties.refreshTokenExpireDays.days.toJavaDuration().toMillis())

        return Jwts.builder()
            .subject(userId)
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
        } catch (e: ExpiredJwtException) {
            null
        } catch (e: JwtException) {
            null
        }
    }

    val secretKey: SecretKey get() = secret
}