package pl.kacosmetology.api.service

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.AUTO
import jakarta.persistence.Id
import jakarta.persistence.Version
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
import java.time.LocalDateTime

@Entity
data class Service(
    @Id
    @GeneratedValue(strategy = AUTO)
    val id: Long? = null,

    val name: String,

    val category: String,

    val description: String,

    val price: Int,

    val duration: Int,

    val isAvailable: Boolean? = true,

    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,

    @Version
    val version: Long? = null
) : Serializable


