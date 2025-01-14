package pl.kacosmetology.api.service


data class ServiceUpdateRequest(

    val name: String? = null,

    val category: String? = null,

    val description: String? = null,

    val price: Int? = null,

    val duration: Int? = null
)
