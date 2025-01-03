package pl.kacosmetology.api.auth.models.responses

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
) {

}
