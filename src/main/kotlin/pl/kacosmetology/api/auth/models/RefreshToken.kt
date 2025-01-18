package pl.kacosmetology.api.auth.models


import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.io.Serializable

@RedisHash(value = "refresh_token")
data class RefreshToken(
    @Id
    val token: String,

    val email: String,

    @TimeToLive
    val ttl: Long
) : Serializable
