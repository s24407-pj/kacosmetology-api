package pl.kacosmetology.api.account

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import pl.kacosmetology.api.account.Role.ROLE_ADMIN
import java.time.LocalDateTime


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Account(
    @Id
    @GeneratedValue
    val id: Long? = null,

    val firstName: String,

    val lastName: String,

    @Column(unique = true)
    val email: String,

    @Column(unique = true)
    val phoneNumber: String,

    val accountPassword: String,

    @CreationTimestamp
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,

    @Version
    val version: Long? = null,

    @Enumerated(EnumType.STRING)
    val role: Role

) : UserDetails {

    fun isAdmin() = role == ROLE_ADMIN

    override fun getAuthorities(): MutableCollection<GrantedAuthority> =
        mutableListOf(GrantedAuthority { role.name })

    override fun getPassword(): String = accountPassword

    override fun getUsername(): String = email
}