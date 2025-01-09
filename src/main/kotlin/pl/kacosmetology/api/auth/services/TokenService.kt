package pl.kacosmetology.api.auth.services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import pl.kacosmetology.api.auth.models.TokenType
import pl.kacosmetology.api.config.JwtProperties
import java.util.*

@Service
class TokenService(
    private val jwtProperties: JwtProperties
) {

    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    fun generate(
        userDetails: UserDetails,
        tokenType: TokenType,
        additionalClaims: MutableMap<String, Any> = mutableMapOf()
    ): String {
        val expirationTime = when (tokenType) {
            TokenType.ACCESS -> Date(jwtProperties.accessTokenExpirationSeconds * 1000 + System.currentTimeMillis())
            TokenType.REFRESH -> Date(jwtProperties.refreshTokenExpirationSeconds * 1000 + System.currentTimeMillis())
        }

        additionalClaims["type"] = tokenType.name

        return Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationTime)
            .add(additionalClaims)
            .and()
            .signWith(secretKey)
            .compact()
    }

    fun extractTokenType(token: String): TokenType? =
        TokenType.valueOf(
            getAllClaims(token)
                .get("type", String::class.java)
        )

    fun extractEmail(token: String): String? =
        getAllClaims(token)
            .subject

    fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    fun getExpirationDate(token: String): Date =
        getAllClaims(token)
            .expiration

    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)

        return email == userDetails.username && !isExpired(token)
    }

    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser
            .parseSignedClaims(token)
            .payload
    }
}