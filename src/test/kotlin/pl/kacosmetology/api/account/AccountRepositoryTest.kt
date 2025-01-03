package pl.kacosmetology.api.account

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DataJpaTest
class AccountRepositoryTest(
    @Autowired
    val underTest: AccountRepository
) {
    private val account = Account(
        email = "test@email.com",
        firstName = "testemailcom",
        lastName = "testemailcom",
        phoneNumber = "555556789",
        password = "password",
    )

    @Test
    fun `should return false when email and phone not found`() {
        // given
        val email = "email@test.pl"
        val phoneNumber = "123456789"
        underTest.save(account)
        // when
        val result = underTest.existsAccountByEmailOrPhoneNumber(email, phoneNumber)

        // then
        assertFalse(result)
    }

    @Test
    fun `should return true when email found`() {
        // given
        val email = account.email
        val phoneNumber = "123456789"
        underTest.save(account)
        // when
        val result = underTest.existsAccountByEmailOrPhoneNumber(email, phoneNumber)

        // then
        assertTrue(result)
    }

    @Test
    fun `should return true when phone number found`() {
        // given
        val email = "someemail@wp.pl"
        val phoneNumber = account.phoneNumber
        underTest.save(account)
        // when
        val result = underTest.existsAccountByEmailOrPhoneNumber(email, phoneNumber)

        // then
        assertTrue(result)
    }

    @Test
    fun `should find account by email`() {
        // given
        underTest.save(account)

        // when
        val result = underTest.findByEmail(account.email)

        // then
        assertEquals(account, result)
    }

    @Test
    fun `should return null when account not found by email`() {
        // given
        underTest.save(account)
        val email = "email@wpl.pl"

        // when
        val result = underTest.findByEmail(email)

        // then
        assertNull(result)
    }
}