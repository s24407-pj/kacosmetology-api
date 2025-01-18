package pl.kacosmetology.api.account.admin

import org.springframework.data.jpa.repository.JpaRepository

interface AdminRepository : JpaRepository<Admin, Long> {
    fun existsAccountByEmailOrPhoneNumber(email: String, phoneNumber: String): Boolean
    fun findByEmail(username: String): Admin?
}