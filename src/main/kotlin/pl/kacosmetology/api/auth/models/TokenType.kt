package pl.kacosmetology.api.auth.models

enum class TokenType(
    val type: String
) {
    ACCESS("access"),
    REFRESH("refresh")
}
