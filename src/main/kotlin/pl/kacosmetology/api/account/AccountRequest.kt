package pl.kacosmetology.api.account

data class AccountRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val password: String
) {

}
