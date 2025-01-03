package pl.kacosmetology.api.auth.models


import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.io.Serializable

@RedisHash(value = "refresh_token", timeToLive = 86400)
data class RefreshToken(
    @Id
    val token: String,
    val email: String
) : Serializable
