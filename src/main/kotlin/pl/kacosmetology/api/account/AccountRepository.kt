package pl.kacosmetology.api.account

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AccountRepository : JpaRepository<Account, Long> {
    fun existsAccountByEmailOrPhoneNumber(email: String, phoneNumber: String): Boolean

    @Query("SELECT new pl.kacosmetology.api.account.AccountDto( a.email, a.accountPassword, a.role) FROM Account a WHERE a.email = :email")
    fun findByEmail(email: String): AccountDto?


}