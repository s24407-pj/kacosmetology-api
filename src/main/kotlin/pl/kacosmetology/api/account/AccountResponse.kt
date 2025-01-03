package pl.kacosmetology.api.account

import java.util.*

data class AccountResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String
) {

}
