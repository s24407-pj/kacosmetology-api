package pl.kacosmetology.api.auth.models

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.io.Serializable

@RedisHash(value = "black_list_token")
data class BlackListToken(
    @Id
    val token: String,
    @TimeToLive
    val ttl: Long
) : Serializable
