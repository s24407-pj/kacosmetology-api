package pl.kacosmetology.api.auth.models.responses

import jakarta.servlet.http.Cookie

data class GeneratedTokens(
    val accessToken: String,
    val expires: Long,
    val refreshTokenCookie: Cookie
)
