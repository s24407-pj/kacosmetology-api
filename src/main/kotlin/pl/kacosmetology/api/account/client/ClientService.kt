package pl.kacosmetology.api.account.client

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.kacosmetology.api.exception.ResourceConflictException
import pl.kacosmetology.api.exception.ResourceNotFoundException

@Service
class ClientService(
    private val clientRepository: ClientRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun createAccount(client: Client): Long {
        if (clientRepository.existsAccountByEmailOrPhoneNumber(client.email, client.phoneNumber)) {
            throw ResourceConflictException("Konto z podanym adresem email lub numerem telefonu już istnieje")
        }

        val accountWithHashedPwd = client.copy(
            accountPassword = passwordEncoder.encode(client.password),
        )

        val savedAccount = clientRepository.save(accountWithHashedPwd)

        return savedAccount.id!!
    }

    fun getByEmail(email: String): Client {
        return clientRepository.findByEmail(email) ?: throw ResourceNotFoundException("Nie znaleziono użytkownika")
    }

    fun getAllAccounts(): List<Client> {
        return clientRepository.findAll()
    }
}