package pl.kacosmetology.api.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jwt")
data class JwtProperties(
    val key: String,
    val accessTokenExpirationSeconds: Long,
    val refreshTokenExpirationSeconds: Long
)
