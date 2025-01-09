package pl.kacosmetology.api.auth.services

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import pl.kacosmetology.api.auth.models.RefreshToken
import pl.kacosmetology.api.auth.models.TokenType.ACCESS
import pl.kacosmetology.api.auth.models.TokenType.REFRESH
import pl.kacosmetology.api.auth.models.requests.AuthRequest
import pl.kacosmetology.api.auth.models.responses.AuthResponse
import pl.kacosmetology.api.auth.repositories.RefreshTokenRepository
import pl.kacosmetology.api.config.JwtProperties
import pl.kacosmetology.api.exception.InvalidTokenException

@Service
class AuthService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val blacklistTokenService: BlacklistTokenService
) {
    fun authenticate(authRequest: AuthRequest): AuthResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )

        val user = userDetailsService.loadUserByUsername(authRequest.email)

        val accessToken = tokenService.generate(user, ACCESS)
        val refreshToken = tokenService.generate(user, REFRESH)

        refreshTokenRepository.save(
            RefreshToken(
                email = authRequest.email,
                token = refreshToken,
                ttl = jwtProperties.refreshTokenExpirationSeconds
            )
        )

        return AuthResponse(accessToken, refreshToken, jwtProperties.accessTokenExpirationSeconds)
    }

    fun refreshAccessToken(token: String): AuthResponse {
        if (blacklistTokenService.isBlacklisted(token))
            throw InvalidTokenException("Niepoprawny token")

        val email = tokenService.extractEmail(token) ?: throw InvalidTokenException("Niepoprawny token")

        val user = userDetailsService.loadUserByUsername(email)
        val refreshToken =
            refreshTokenRepository.findById(token).orElseThrow { InvalidTokenException("Niepoprawny token") }

        if (tokenService.isExpired(token) || user.username != refreshToken.email) {
            throw InvalidTokenException("Niepoprawny token")
        }

        blacklistTokenService.addToBlacklist(token)

        val newAccessToken = tokenService.generate(user, ACCESS)
        val newRefreshToken = tokenService.generate(user, REFRESH)

        refreshTokenRepository.save(
            RefreshToken(
                email = email,
                token = newRefreshToken,
                ttl = jwtProperties.refreshTokenExpirationSeconds
            )
        )

        return AuthResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            expires = jwtProperties.accessTokenExpirationSeconds
        )
    }

    fun logout(token: String) {
        blacklistTokenService.addToBlacklist(
            token = token
        )
    }

}
