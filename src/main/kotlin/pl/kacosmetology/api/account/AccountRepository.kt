package pl.kacosmetology.api.account

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : JpaRepository<Account, UUID> {
    fun existsAccountByEmailOrPhoneNumber(email: String, phoneNumber: String): Boolean
    fun findByEmail(username: String): Account?
}