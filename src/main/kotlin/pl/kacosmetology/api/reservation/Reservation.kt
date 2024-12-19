package pl.kacosmetology.api.reservation

import jakarta.persistence.*
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
    val notes: String?,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
) {
    fun toResponse() = ReservationResponse(
        id = id!!,
        firstName = firstName,
        lastName = lastName,
        appointmentDateTime = appointmentDateTime,
        email = email,
        phoneNumber = phoneNumber,
        status = PENDING,
        notes = notes,
        serviceId = serviceId
    )
}