package pl.kacosmetology.api.account

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockKExtension::class)
class AccountServiceTest {
    //TODO
    @MockK
    lateinit var accountRepositoryMock: AccountRepository

    @MockK
    lateinit var encoderMock: PasswordEncoder

    @InjectMockKs
    lateinit var underTest: AccountService

    @Test
    fun `should create account`() {

    }

    @Test
    fun `should get all accounts`() {
    }
}