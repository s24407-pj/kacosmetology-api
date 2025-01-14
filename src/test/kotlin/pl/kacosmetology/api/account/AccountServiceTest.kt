package pl.kacosmetology.api.account

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder
import pl.kacosmetology.api.account.Gender.MALE
import pl.kacosmetology.api.exception.ResourceConflictException
import pl.kacosmetology.api.exception.ResourceNotFoundException
import java.util.*

@ExtendWith(MockKExtension::class)
class AccountServiceTest {

    @MockK
    lateinit var accountRepositoryMock: AccountRepository

    @MockK
    lateinit var encoderMock: PasswordEncoder

    @InjectMockKs
    lateinit var underTest: AccountService

    private val account = Account(
        firstName = "Jan",
        lastName = "Kowalski",
        email = "email@wp.pl",
        phoneNumber = "123456789",
        gender = MALE,
        password = "password123#."
    )
    private val encodedPassword = "encodedPassword"
    private val uuid = UUID.randomUUID()

    @Test
    fun `should create account`() {
        every { encoderMock.encode(account.password) } returns encodedPassword
        every {
            accountRepositoryMock.existsAccountByEmailOrPhoneNumber(
                account.email,
                account.phoneNumber
            )
        } returns false
        every { accountRepositoryMock.save(any()) } answers {
            Account(
                id = uuid,
                firstName = account.firstName,
                lastName = account.lastName,
                email = account.email,
                phoneNumber = account.phoneNumber,
                gender = account.gender,
                password = encodedPassword,
                role = Role.USER
            )
        }

        // When
        val result = underTest.createAccount(account)

        // Then
        verify {
            accountRepositoryMock.save(withArg { savedAccount ->
                assert(savedAccount.firstName == account.firstName)
                assert(savedAccount.lastName == account.lastName)
                assert(savedAccount.email == account.email)
                assert(savedAccount.phoneNumber == account.phoneNumber)
                assert(savedAccount.password == encodedPassword)
                assert(savedAccount.role == Role.USER)
            })
        }

        assert(result == uuid)
    }

    @Test
    fun `should throw exception when account already exists`() {
        // Given
        every {
            accountRepositoryMock.existsAccountByEmailOrPhoneNumber(
                account.email,
                account.phoneNumber
            )
        } returns true

        // When
        try {
            underTest.createAccount(account)
        } catch (e: ResourceConflictException) {
            // Then
            assert(true)
        }
    }

    @Test
    fun `should get all accounts`() {
        // Given
        every { accountRepositoryMock.findAll() } returns listOf(account)

        // When
        val result = underTest.getAllAccounts()

        // Then
        assert(result.size == 1)
    }

    @Test
    fun `should get account by email`() {
        // Given
        val email = account.email
        every { accountRepositoryMock.findByEmail(email) } returns account

        // When
        val result = underTest.getByEmail(email)

        // Then
        assert(result == account)
    }

    @Test
    fun `should throw when account by email not found`() {
        // Given
        val email = account.email
        every { accountRepositoryMock.findByEmail(email) } returns null

        // When
        try {
            underTest.getByEmail(email)
        } catch (e: ResourceNotFoundException) {
            // Then
            assert(true)
        }
    }
}