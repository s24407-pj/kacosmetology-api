package pl.kacosmetology.api.auth.services

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import pl.kacosmetology.api.client.Client
import pl.kacosmetology.api.client.ClientRepository

@Service
class CustomUserDetailsService(
    private val clientRepository: ClientRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        clientRepository.findByEmail(username)
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("Nie znaleziono użytkownika")

    private fun Client.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.email)
            .password(this.password)
            .build()
}