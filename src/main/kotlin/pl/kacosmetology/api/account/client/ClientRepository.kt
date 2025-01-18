package pl.kacosmetology.api.account.client

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Client, Long> {
    fun existsAccountByEmailOrPhoneNumber(email: String, phoneNumber: String): Boolean
    fun findByEmail(username: String): Client?
}