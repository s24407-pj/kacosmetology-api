package pl.kacosmetology.api.auth

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.kacosmetology.api.auth.repositories.BlacklistTokenRepository
import pl.kacosmetology.api.auth.services.BlacklistTokenService
import pl.kacosmetology.api.auth.services.TokenService
import java.util.*

@ExtendWith(MockKExtension::class)
class BlacklistTokenServiceTest {
    @MockK
    private lateinit var blacklistTokenRepositoryMock: BlacklistTokenRepository

    @MockK
    private lateinit var tokenServiceMock: TokenService

    @InjectMockKs
    private lateinit var underTest: BlacklistTokenService

    @Test
    fun `should add token to blacklist`() {
        // given
        val token = "token"

        every { tokenServiceMock.getExpirationDate(token) } returns Date()
        every { blacklistTokenRepositoryMock.save(any()) } returns mockk()

        // when
        underTest.addToBlacklist(token)

        // then
        verify { blacklistTokenRepositoryMock.save(any()) }
    }

    @Test
    fun `should check if token is blacklisted`() {
        // given
        val token = "token"

        every { blacklistTokenRepositoryMock.existsById(token) } returns true

        // when
        val result = underTest.isBlacklisted(token)

        // then
        assert(result)
    }


}