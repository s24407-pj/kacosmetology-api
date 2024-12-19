package pl.kacosmetology.api.reservation

import java.time.LocalDateTime
import java.util.*

data class ReservationResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val appointmentDateTime: LocalDateTime,
    val email: String,
    val phoneNumber: String,
    val status: ReservationStatus,
    val notes: String?,
    val serviceId: Int
)
