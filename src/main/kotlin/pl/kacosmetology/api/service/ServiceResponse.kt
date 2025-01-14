package pl.kacosmetology.api.service

data class ServiceResponse(
    val id: Long,
    val name: String,
    val category: String,
    val description: String,
    val price: Int,
    val duration: Int,
)
