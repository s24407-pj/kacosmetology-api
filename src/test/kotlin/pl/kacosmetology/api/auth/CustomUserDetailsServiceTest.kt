package pl.kacosmetology.api.auth

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.userdetails.UsernameNotFoundException
import pl.kacosmetology.api.client.Client
import pl.kacosmetology.api.client.ClientRepository
import pl.kacosmetology.api.client.Gender.FEMALE
import pl.kacosmetology.api.auth.services.CustomUserDetailsService

@ExtendWith(MockKExtension::class)
class CustomUserDetailsServiceTest {


    private val client = Client(
        firstName = "Janusz",
        lastName = "Kowalski",
        email = "test@email.com",
        phoneNumber = "123456789",
        password = "password",
        gender = FEMALE
    )

    @MockK
    lateinit var mockRepository: ClientRepository

    @InjectMockKs
    lateinit var underTestService: CustomUserDetailsService

    @Test
    fun `should load by username`() {
        // Given

        every { mockRepository.findByEmail(any()) } returns client


        // When
        val result = underTestService.loadUserByUsername(client.email)

        // Then
        assert(result.username == client.email)
        assert(result.password == client.password)

    }

    @Test
    fun `should throw exception when user not found`() {
        // Given
        every { mockRepository.findByEmail(any()) } returns null

        // When
        val exception = assertThrows<UsernameNotFoundException> {
            underTestService.loadUserByUsername(client.email)
        }

        // Then
        assert(exception.message == "Nie znaleziono użytkownika")
    }
}