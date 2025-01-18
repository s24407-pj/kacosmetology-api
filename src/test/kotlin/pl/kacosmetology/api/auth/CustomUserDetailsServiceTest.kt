package pl.kacosmetology.api.auth

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import pl.kacosmetology.api.account.AccountDto
import pl.kacosmetology.api.account.AccountRepository
import pl.kacosmetology.api.account.Role
import pl.kacosmetology.api.auth.services.CustomUserDetailsService
import pl.kacosmetology.api.exception.ResourceNotFoundException

@ExtendWith(MockKExtension::class)
class CustomUserDetailsServiceTest {


    private val account = AccountDto(
        email = "test@email.com",
        accountPassword = "password",
        role = Role.ROLE_USER
    )

    @MockK
    lateinit var mockRepository: AccountRepository

    @InjectMockKs
    lateinit var underTestService: CustomUserDetailsService

    @Test
    fun `should load by username`() {
        // Given

        every { mockRepository.findByEmail(any()) } returns account


        // When
        val result = underTestService.loadUserByUsername(account.email)

        // Then
        assert(result != null)
        assert(result?.username == account.email)
        assert(result?.password == account.accountPassword)

    }

    @Test
    fun `should throw exception when user not found`() {
        // Given
        every { mockRepository.findByEmail(any()) } returns null

        // When
        assertThrows<ResourceNotFoundException> {
            underTestService.loadUserByUsername(account.email)
        }


    }
}