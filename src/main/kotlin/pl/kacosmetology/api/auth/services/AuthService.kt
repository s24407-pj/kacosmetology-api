package pl.kacosmetology.api.auth.services

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import pl.kacosmetology.api.auth.RefreshTokenRepository
import pl.kacosmetology.api.auth.models.RefreshToken
import pl.kacosmetology.api.auth.models.requests.AuthRequest
import pl.kacosmetology.api.auth.models.responses.AuthResponse
import pl.kacosmetology.api.config.JwtProperties
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class AuthService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun authenticate(authRequest: AuthRequest): AuthResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )

        val user = userDetailsService.loadUserByUsername(authRequest.email)
        val accessToken = generateToken(user, jwtProperties.accessTokenExpiration)
        val refreshToken = generateToken(user, jwtProperties.refreshTokenExpiration)

        refreshTokenRepository.save(
            RefreshToken(
                email = authRequest.email,
                token = refreshToken,
            )
        )


        return AuthResponse(accessToken, refreshToken)
    }

    fun refreshAccessToken(token: String): String {
        val extractedEmail = tokenService.extractEmail(token) ?: throw IllegalArgumentException("Invalid token")

        return extractedEmail.let { email ->
            val currentUserDetails = userDetailsService.loadUserByUsername(email)
            val refreshToken =
                refreshTokenRepository.findById(token).getOrElse { throw IllegalArgumentException("Invalid token") }

            if (!tokenService.isExpired(token) && currentUserDetails.username == refreshToken.email)
                generateToken(currentUserDetails, jwtProperties.accessTokenExpiration)
            else
                throw IllegalArgumentException("Invalid token")
        }
    }

    private fun generateToken(user: UserDetails, expirationTime: Long): String =
        tokenService.generate(
            user,
            Date(System.currentTimeMillis() + expirationTime)
        )
}
