package pl.kacosmetology.api.auth.services

import jakarta.servlet.http.Cookie
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import pl.kacosmetology.api.auth.models.RefreshToken
import pl.kacosmetology.api.auth.models.TokenType.ACCESS
import pl.kacosmetology.api.auth.models.TokenType.REFRESH
import pl.kacosmetology.api.auth.models.requests.AuthRequest
import pl.kacosmetology.api.auth.models.responses.GeneratedTokens
import pl.kacosmetology.api.auth.repositories.RefreshTokenRepository
import pl.kacosmetology.api.config.JwtProperties
import pl.kacosmetology.api.exception.InvalidTokenException

@Service
class AuthService(
    private val authManager: AuthenticationManager,
    private val customUserDetailsService: CustomUserDetailsService,

    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun authenticate(authRequest: AuthRequest): GeneratedTokens {

        val email = authRequest.email

        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                email,
                authRequest.password
            )
        )

        val user = customUserDetailsService.loadUserByUsername(email) ?: throw InvalidTokenException("Niepoprawne dane")

        return generateTokens(user)
    }


    fun refreshAccessToken(refreshToken: String): GeneratedTokens {

        val email = tokenService.extractEmail(refreshToken) ?: throw InvalidTokenException("Niepoprawny token")
        val foundToken =
            refreshTokenRepository.findById(refreshToken).orElseThrow { InvalidTokenException("Niepoprawny token") }

        val user =
            customUserDetailsService.loadUserByUsername(email) ?: throw InvalidTokenException("Niepoprawny token")

        if (tokenService.isExpired(refreshToken) || foundToken.email != email) {
            throw InvalidTokenException("Niepoprawny token")
        }

        refreshTokenRepository.deleteById(refreshToken)

        return generateTokens(user)
    }

    private fun generateTokens(
        user: UserDetails
    ): GeneratedTokens {
        val accessToken = tokenService.generate(user, ACCESS)
        val refreshToken = tokenService.generate(user, REFRESH)

        refreshTokenRepository.save(
            RefreshToken(
                email = user.username,
                token = refreshToken,
                ttl = jwtProperties.refreshTokenExpirationSeconds
            )
        )

        val refreshTokenCookie = Cookie("refreshToken", refreshToken).apply {
            isHttpOnly = true
            maxAge = jwtProperties.refreshTokenExpirationSeconds.toInt()
            secure = true
            path = "/api/v1/auth/refresh"
        }

        return GeneratedTokens(
            accessToken = accessToken,
            expires = jwtProperties.accessTokenExpirationSeconds,
            refreshTokenCookie = refreshTokenCookie
        )
    }

    fun logout(token: String): Cookie {
        val deletedRefreshTokenCookie = Cookie("refreshToken", "").apply {
            isHttpOnly = true
            maxAge = 0
            secure = true
            path = "/api/v1/auth/refresh"
        }

        return deletedRefreshTokenCookie
    }

}
