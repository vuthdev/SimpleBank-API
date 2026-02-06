package firestorm.vuth.simplebank.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    @DefaultValue("DTJ3sSzD52f7UBBnGppr2bo82BfHXjetX72yF6ANZrqjBKujjLpSSMHjdg==") val secret: String,
    @DefaultValue("15") val accessTokenExpireMinutes: Long = 15,
    @DefaultValue("7") val refreshTokenExpireMinutes: Long = 7,
    @DefaultValue("SimpleBank") val issuer: String = "SimpleBank",
)