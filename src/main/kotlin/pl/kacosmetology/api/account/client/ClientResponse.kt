package pl.kacosmetology.api.account.client

data class ClientResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: Gender,
    val phoneNumber: String
)
