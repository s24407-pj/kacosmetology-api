package pl.kacosmetology.api.auth.services

import jakarta.transaction.Transactional
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import pl.kacosmetology.api.account.AccountDto
import pl.kacosmetology.api.account.AccountRepository
import pl.kacosmetology.api.exception.ResourceNotFoundException

@Transactional
@Service
class CustomUserDetailsService(
    private val accountRepository: AccountRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? {
        val account = accountRepository.findByEmail(username)
            ?.toUserDetails()
            ?: throw ResourceNotFoundException("Nie znaleziono konta")

        return account
    }
}

private fun AccountDto.toUserDetails(): UserDetails =
    User(
        email,
        accountPassword,
        role.let { listOf(GrantedAuthority { it.name }) }
    )
