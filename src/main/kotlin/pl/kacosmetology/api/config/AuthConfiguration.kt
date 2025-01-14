package pl.kacosmetology.api.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pl.kacosmetology.api.account.AccountRepository
import pl.kacosmetology.api.auth.services.CustomUserDetailsService

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class AuthConfiguration {

    @Bean
    fun userDetailsService(accountRepository: AccountRepository): UserDetailsService =
        CustomUserDetailsService(accountRepository)

    @Bean
    fun encoder() = BCryptPasswordEncoder()

    @Bean
    fun authProvider(accountRepository: AccountRepository): AuthenticationProvider =
        DaoAuthenticationProvider()
            .also {
                it.setUserDetailsService(userDetailsService(accountRepository))
                it.setPasswordEncoder(encoder())
            }

    @Bean
    fun authManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
}