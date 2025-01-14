package pl.kacosmetology.api.account

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import pl.kacosmetology.api.account.Role.USER
import java.time.LocalDateTime
import java.util.*

@Entity
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    val firstName: String,

    val lastName: String,

    @Column(unique = true)
    val email: String,

    @Column(unique = true)
    val phoneNumber: String,

    @Enumerated(EnumType.STRING)
    val gender: Gender,

    val password: String,

    val role: Role = USER,

    @CreationTimestamp
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,

    @Version
    val version: Long? = null
)
