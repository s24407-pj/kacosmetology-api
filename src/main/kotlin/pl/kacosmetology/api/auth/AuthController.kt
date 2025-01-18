package pl.kacosmetology.api.auth

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.kacosmetology.api.auth.models.requests.AuthRequest
import pl.kacosmetology.api.auth.models.requests.RefreshTokenRequest
import pl.kacosmetology.api.auth.models.responses.AuthResponse
import pl.kacosmetology.api.auth.services.AuthService


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping
    fun authenticate(@RequestBody authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        val authResponse = authService.authenticate(authRequest)

        return ResponseEntity(authResponse, OK)
    }

    @PostMapping("/refresh")
    fun refreshAccessToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<AuthResponse> {
        val authResponse = authService.refreshAccessToken(refreshTokenRequest)

        return ResponseEntity(authResponse, OK)
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Unit> {
        val token: String = request.getHeader("Authorization").substringAfter("Bearer ")

        authService.logout(token)

        return ResponseEntity(OK)
    }
}