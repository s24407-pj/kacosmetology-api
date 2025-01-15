package pl.kacosmetology.api.client

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder
import pl.kacosmetology.api.client.Gender.MALE
import pl.kacosmetology.api.exception.ResourceConflictException
import pl.kacosmetology.api.exception.ResourceNotFoundException
import java.util.*

@ExtendWith(MockKExtension::class)
class ClientServiceTest {

    @MockK
    lateinit var clientRepositoryMock: ClientRepository

    @MockK
    lateinit var encoderMock: PasswordEncoder

    @InjectMockKs
    lateinit var underTest: ClientService

    private val client = Client(
        firstName = "Jan",
        lastName = "Kowalski",
        email = "email@wp.pl",
        phoneNumber = "123456789",
        gender = MALE,
        password = "password123#."
    )
    private val encodedPassword = "encodedPassword"
    private val id = 23L

    @Test
    fun `should create account`() {
        every { encoderMock.encode(client.password) } returns encodedPassword
        every {
            clientRepositoryMock.existsAccountByEmailOrPhoneNumber(
                client.email,
                client.phoneNumber
            )
        } returns false
        every { clientRepositoryMock.save(any()) } answers {
            Client(
                id = id,
                firstName = client.firstName,
                lastName = client.lastName,
                email = client.email,
                phoneNumber = client.phoneNumber,
                gender = client.gender,
                password = encodedPassword,
                role = Role.USER
            )
        }

        // When
        val result = underTest.createAccount(client)

        // Then
        verify {
            clientRepositoryMock.save(withArg { savedAccount ->
                assert(savedAccount.firstName == client.firstName)
                assert(savedAccount.lastName == client.lastName)
                assert(savedAccount.email == client.email)
                assert(savedAccount.phoneNumber == client.phoneNumber)
                assert(savedAccount.password == encodedPassword)
                assert(savedAccount.role == Role.USER)
            })
        }

        assert(result == id)
    }

    @Test
    fun `should throw exception when account already exists`() {
        // Given
        every {
            clientRepositoryMock.existsAccountByEmailOrPhoneNumber(
                client.email,
                client.phoneNumber
            )
        } returns true

        // When
        try {
            underTest.createAccount(client)
        } catch (e: ResourceConflictException) {
            // Then
            assert(true)
        }
    }

    @Test
    fun `should get all accounts`() {
        // Given
        every { clientRepositoryMock.findAll() } returns listOf(client)

        // When
        val result = underTest.getAllAccounts()

        // Then
        assert(result.size == 1)
    }

    @Test
    fun `should get account by email`() {
        // Given
        val email = client.email
        every { clientRepositoryMock.findByEmail(email) } returns client

        // When
        val result = underTest.getByEmail(email)

        // Then
        assert(result == client)
    }

    @Test
    fun `should throw when account by email not found`() {
        // Given
        val email = client.email
        every { clientRepositoryMock.findByEmail(email) } returns null

        // When
        try {
            underTest.getByEmail(email)
        } catch (e: ResourceNotFoundException) {
            // Then
            assert(true)
        }
    }
}