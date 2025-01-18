package pl.kacosmetology.api.auth.models.requests

data class RefreshTokenRequest(
    val token: String,
    val isAdmin: Boolean = false
)
