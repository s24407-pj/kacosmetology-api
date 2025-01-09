package pl.kacosmetology.api.reservation

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import pl.kacosmetology.api.reservation.ReservationStatus.PENDING
import java.time.LocalDateTime
import java.util.*

@Entity
data class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    val firstName: String,

    val lastName: String,

    val appointmentDateTime: LocalDateTime,

    val serviceId: Int, //TODO: Implement service

    val email: String,

    val phoneNumber: String,

    @Enumerated(EnumType.STRING)
    val status: ReservationStatus = PENDING,

    val notes: String? = null,

    @CreationTimestamp
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,

    @Version
    val version: Long? = null
)