package pl.kacosmetology.api.client

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pl.kacosmetology.api.account.client.Client
import pl.kacosmetology.api.account.client.ClientRepository
import pl.kacosmetology.api.account.client.Gender.MALE
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DataJpaTest
class ClientRepositoryTest(
    @Autowired
    val underTest: ClientRepository
) {
    private val client = Client(
        email = "test@email.com",
        firstName = "testemailcom",
        lastName = "testemailcom",
        phoneNumber = "555556789",
        gender = MALE,
        accountPassword = "password2!AA",
    )

    @Test
    fun `should return false when account with provided email and phone not found`() {
        // given
        val email = "email@test.pl"
        val phoneNumber = "123456789"
        underTest.save(client)
        // when
        val result = underTest.existsAccountByEmailOrPhoneNumber(email, phoneNumber)

        // then
        assertFalse(result)
    }

    @Test
    fun `should return true when account with provided email found`() {
        // given
        val email = client.email
        val phoneNumber = "123456789"
        underTest.save(client)
        // when
        val result = underTest.existsAccountByEmailOrPhoneNumber(email, phoneNumber)

        // then
        assertTrue(result)
    }

    @Test
    fun `should return true when account with provided phone number found`() {
        // given
        val email = "someemail@wp.pl"
        val phoneNumber = client.phoneNumber
        underTest.save(client)
        // when
        val result = underTest.existsAccountByEmailOrPhoneNumber(email, phoneNumber)

        // then
        assertTrue(result)
    }

    @Test
    fun `should find account by email`() {
        // given
        underTest.save(client)

        // when
        val result = underTest.findByEmail(client.email)

        // then
        assertEquals(client, result)
    }

    @Test
    fun `should return null when account not found by email`() {
        // given
        underTest.save(client)
        val email = "email@wpl.pl"

        // when
        val result = underTest.findByEmail(email)

        // then
        assertNull(result)
    }
}