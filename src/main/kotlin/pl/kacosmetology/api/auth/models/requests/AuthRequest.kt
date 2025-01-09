package pl.kacosmetology.api.auth.models.requests

data class AuthRequest(
    val email: String,
    val password: String
)
