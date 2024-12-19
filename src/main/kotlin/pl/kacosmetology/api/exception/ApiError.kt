package pl.kacosmetology.api.exception

data class ApiError(
    val statusCode: Int,
    val message: String?
)