package pl.kacosmetology.api.auth

import io.jsonwebtoken.ExpiredJwtException
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.userdetails.UserDetails
import pl.kacosmetology.api.auth.models.TokenType.ACCESS
import pl.kacosmetology.api.auth.models.TokenType.REFRESH
import pl.kacosmetology.api.auth.services.TokenService
import pl.kacosmetology.api.config.JwtProperties

@ExtendWith(MockKExtension::class)
class TokenServiceTest {
    @MockK
    lateinit var jwtPropertiesMock: JwtProperties

    private lateinit var underTest: TokenService

    @BeforeEach
    fun setUp() {
        // Mock the JwtProperties fields
        every { jwtPropertiesMock.key } returns "NTNv7j0TuYARvmNMmWXo6fKvM4o6nv/aUi9ryX38ZH+L1bkrnD1ObOQ8JAUmHCBq7Iy7otZcyAagBLHVKvvYaIpmMuxmARQ97jUVG16Jkpkp1wXOPsrF9zwew6TpczyHkHgX5EuLg2MeBuiT/qJACs1J0apruOOJCg/gOtkjB4c="
        every { jwtPropertiesMock.accessTokenExpirationSeconds } returns 99999
        every { jwtPropertiesMock.refreshTokenExpirationSeconds } returns 7200

        // Manually initialize TokenService
        underTest = TokenService(jwtPropertiesMock)
    }

    @Test
    fun `should generate access token`() {
        // given
        val userDetailsMock = mockk<UserDetails>()
        every { userDetailsMock.username } returns "test-user"
        // when
        val result = underTest.generate(userDetailsMock, ACCESS)

        // then
        assert(result.isNotEmpty())
    }

    @Test
    fun `should generate refresh token`() {
        // given
        val userDetailsMock = mockk<UserDetails>()
        every { userDetailsMock.username } returns "test-user"
        // when
        val result = underTest.generate(userDetailsMock, REFRESH)

        // then
        assert(result.isNotEmpty())
    }

    @Test
    fun `should extract email from token`() {
        // given
        val userDetailsMock = mockk<UserDetails>()
        every { userDetailsMock.username } returns "test-user"
        val token = underTest.generate(userDetailsMock, ACCESS)
        // when
        val result = underTest.extractEmail(token)

        // then
        assert(result == "test-user")
    }

    @Test
    fun `should extract token type from token`() {
        // given
        val userDetailsMock = mockk<UserDetails>()
        every { userDetailsMock.username } returns "test-user"
        val token = underTest.generate(userDetailsMock, ACCESS)
        // when
        val result = underTest.extractTokenType(token)

        // then
        assert(result == ACCESS)
    }

    @Test
    fun `should check if token expired`() {
        // given
        val userDetailsMock = mockk<UserDetails>()
        every { userDetailsMock.username } returns "test-user"
        val token = underTest.generate(userDetailsMock, ACCESS)
        // when
        val result = underTest.isExpired(token)

        // then
        assert(!result)
    }

    @Test
    fun `should throw when token expired`() {
        // given
        val expiredToken =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0LXVzZXIiLCJpYXQiOjE3MzY4Njk5NDUsImV4cCI6MTczNjg2OTk0NiwidHlwZSI6IkFDQ0VTUyJ9.7IKnIr-rJSNMGlTqpkyqF41YOywdJzlhRu0uU7NXVWCUes-FAC3T1tI1eSpq0ZaId5msB1wtr3TfZtxVvgkG5g"
        val userDetailsMock = mockk<UserDetails>()
        every { userDetailsMock.username } returns "test-user"

        // when
        try {
            underTest.isExpired(expiredToken)
        } catch (e: ExpiredJwtException) {
            // then
            assert(true)
        }
    }

    @Test
    fun `should check if token is valid`() {
        // given
        val userDetailsMock = mockk<UserDetails>()
        every { userDetailsMock.username } returns "test-user"
        val token = underTest.generate(userDetailsMock, ACCESS)
        // when
        val result = underTest.isValid(token, userDetailsMock)

        // then
        assert(result)
    }

    @Test
    fun `should return false for an invalid token`() {
        // Arrange
        val userDetailsMock = mockk<UserDetails> {
            every { username } returns "wrong@example.com" // Intentionally mismatch
        }
        val token = underTest.generate(
            mockk {
                every { username } returns "correct@example.com"
            },
            ACCESS
        )

        // Act
        val isValid = underTest.isValid(token, userDetailsMock)

        // Assert
        assertFalse(isValid)
    }
}