package pl.kacosmetology.api.auth

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetails
import pl.kacosmetology.api.auth.models.RefreshToken
import pl.kacosmetology.api.auth.models.requests.AuthRequest
import pl.kacosmetology.api.auth.services.AuthService
import pl.kacosmetology.api.auth.services.CustomUserDetailsService
import pl.kacosmetology.api.auth.services.TokenService
import pl.kacosmetology.api.config.JwtProperties
import java.util.*

@ExtendWith(MockKExtension::class)
class AuthServiceTest {

    @MockK
    lateinit var mockRefreshTokenRepository: RefreshTokenRepository

    @MockK
    lateinit var mockAuthManager: AuthenticationManager

    @MockK
    lateinit var mockUserDetailsService: CustomUserDetailsService

    @MockK
    lateinit var mockTokenService: TokenService

    @MockK
    lateinit var mockJwtProperties: JwtProperties


    @InjectMockKs
    lateinit var underTestService: AuthService

    @Test
    fun `should authenticate`() {
        val authRequest = AuthRequest("email", "password")
        // given
        every { mockUserDetailsService.loadUserByUsername(authRequest.email) } returns mockk()
        every { mockJwtProperties.accessTokenExpiration } returns 1000
        every { mockJwtProperties.refreshTokenExpiration } returns 1000
        every { mockAuthManager.authenticate(any()) } returns mockk()
        every { mockTokenService.generate(any(), any()) } returns "token"
        every { mockRefreshTokenRepository.save(any()) } returns mockk()


        // when
        val result = underTestService.authenticate(authRequest)

        // then

        assert(result.accessToken.isNotEmpty())
        assert(result.refreshToken.isNotEmpty())
        verify { mockAuthManager.authenticate(any()) }
        verify { mockUserDetailsService.loadUserByUsername(authRequest.email) }

    }

    @Test
    fun `should throw exception when bad credential `() {

    }

    @Test
    fun `should refresh the token`() {
        // given
        val token = "refreshToken"
        val email = "email"
        val userMock = mockk<UserDetails>()
        val refreshTokenMock = RefreshToken(token, email)

        every { mockTokenService.extractEmail(token) } returns email
        every { mockUserDetailsService.loadUserByUsername(email) } returns userMock
        every { mockRefreshTokenRepository.findById(token) } returns Optional.of(refreshTokenMock)
        every { mockTokenService.isExpired(token) } returns false
        every { mockJwtProperties.accessTokenExpiration } returns 1000
        every { mockTokenService.generate(any(), any()) } returns "token"
        every { userMock.username } returns email

        // when
        underTestService.refreshAccessToken(token)

        // then
        verify { mockTokenService.extractEmail(token) }
        verify { mockUserDetailsService.loadUserByUsername(email) }
        verify { mockRefreshTokenRepository.findById(token) }
        verify { mockTokenService.isExpired(token) }
        verify { mockTokenService.generate(any(), any()) }
    }

    @Test
    fun `should throw exception when token is invalid`() {
    }
}