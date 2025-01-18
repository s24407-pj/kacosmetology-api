package pl.kacosmetology.api.auth.services

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import pl.kacosmetology.api.auth.models.RefreshToken
import pl.kacosmetology.api.auth.models.TokenType.ACCESS
import pl.kacosmetology.api.auth.models.TokenType.REFRESH
import pl.kacosmetology.api.auth.models.requests.AuthRequest
import pl.kacosmetology.api.auth.models.requests.RefreshTokenRequest
import pl.kacosmetology.api.auth.models.responses.AuthResponse
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
    private val blacklistTokenService: BlacklistTokenService,
) {
    fun authenticate(authRequest: AuthRequest): AuthResponse {

        val email = authRequest.email

        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                email,
                authRequest.password
            )
        )

        val user = customUserDetailsService.loadUserByUsername(email) ?: throw InvalidTokenException("Niepoprawne dane")

        val authResponse = generateTokens(user, email)

        return authResponse
    }


    fun refreshAccessToken(refreshTokenRequest: RefreshTokenRequest): AuthResponse {
        val token = refreshTokenRequest.token

        if (blacklistTokenService.isBlacklisted(token))
            throw InvalidTokenException("Niepoprawny token")

        val email = tokenService.extractEmail(token) ?: throw InvalidTokenException("Niepoprawny token")

        val user =
            customUserDetailsService.loadUserByUsername(email) ?: throw InvalidTokenException("Niepoprawny token")

        val refreshToken =
            refreshTokenRepository.findById(token).orElseThrow { InvalidTokenException("Niepoprawny token") }

        if (tokenService.isExpired(token) || user.username != refreshToken.email) {
            throw InvalidTokenException("Niepoprawny token")
        }

        blacklistTokenService.addToBlacklist(token)

        val authResponse = generateTokens(user, email)

        return authResponse
    }

    private fun generateTokens(
        user: UserDetails,
        email: String
    ): AuthResponse {
        val accessToken = tokenService.generate(user, ACCESS)
        val refreshToken = tokenService.generate(user, REFRESH)

        refreshTokenRepository.save(
            RefreshToken(
                email = email,
                token = refreshToken,
                ttl = jwtProperties.refreshTokenExpirationSeconds
            )
        )



        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expires = jwtProperties.accessTokenExpirationSeconds,

            )
    }

    fun logout(token: String) {
        blacklistTokenService.addToBlacklist(
            token = token
        )
    }

}
