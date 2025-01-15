package pl.kacosmetology.api.client

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import pl.kacosmetology.api.client.Role.USER
import java.time.LocalDateTime

@Entity
data class Client(
    @Id
    @GeneratedValue
    val id: Long? = null,

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
