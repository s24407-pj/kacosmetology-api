package pl.kacosmetology.api.auth

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.kacosmetology.api.auth.models.requests.AuthRequest
import pl.kacosmetology.api.auth.models.responses.AuthResponse
import pl.kacosmetology.api.auth.services.AuthService


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping
    fun authenticate(
        @RequestBody authRequest: AuthRequest,
         response: HttpServletResponse
        ): ResponseEntity<AuthResponse> {
        val generatedTokens = authService.authenticate(authRequest)

        response.addCookie(generatedTokens.refreshTokenCookie)

        val authResponse = AuthResponse(
            accessToken = generatedTokens.accessToken,
            expires = generatedTokens.expires,
        )

        return ResponseEntity(authResponse, OK)
    }

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @CookieValue(value = "refreshToken") refreshToken: String,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponse> {

        val generatedTokens = authService.refreshAccessToken(refreshToken)

        response.addCookie(generatedTokens.refreshTokenCookie)

        val authResponse = AuthResponse(
            accessToken = generatedTokens.accessToken,
            expires = generatedTokens.expires,
        )

        return ResponseEntity(authResponse, OK)
    }

    @PostMapping("/logout")
    fun logout(
        @RequestHeader(AUTHORIZATION) header: String,
        response: HttpServletResponse
    ): ResponseEntity<Unit> {
        val token: String = header.substringAfter("Bearer ")

       val deletedCookie = authService.logout(token)

        response.addCookie(deletedCookie)
        response.addHeader(AUTHORIZATION, "")

        return ResponseEntity(NO_CONTENT)
    }
}