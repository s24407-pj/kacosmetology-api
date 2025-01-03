package pl.kacosmetology.api.account

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.kacosmetology.api.exception.ResourceConflictException
import java.util.*

@Service
class AccountService(private val accountRepository: AccountRepository, private val passwordEncoder: PasswordEncoder) {
    fun createAccount(account: Account): UUID {
        if (accountRepository.existsAccountByEmailOrPhoneNumber(account.email, account.phoneNumber)) {
            throw ResourceConflictException("Konto z podanym adresem email lub numerem telefonu już istnieje")
        }

        val accountWithHashedPwd = account.copy(
            password = passwordEncoder.encode(account.password) //TODO: encode password
        )

        return accountRepository.save(accountWithHashedPwd).id!!
    }

    fun getAllAccounts(): List<Account> {
        return accountRepository.findAll()
    }
}