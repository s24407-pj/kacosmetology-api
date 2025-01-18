package pl.kacosmetology.api.account

import java.io.Serializable

/**
 * DTO for {@link pl.kacosmetology.api.account.Account}
 */
data class AccountDto(val email: String, val accountPassword: String, val role: Role) :
    Serializable