package pl.kacosmetology.api.client

import java.util.*

data class ClientResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: Gender,
    val phoneNumber: String
)
