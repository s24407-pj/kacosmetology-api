package pl.kacosmetology.api.reservation

import java.time.LocalDateTime

data class ReservationRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val appointmentDateTime: LocalDateTime,
    val serviceId: Int,
    val status: ReservationStatus,
    val notes: String?,

    ) {
    fun toModel() =
        Reservation(
            firstName = firstName,
            lastName = lastName,
            appointmentDateTime = appointmentDateTime,
            serviceId = serviceId,
            email = email,
            phoneNumber = phoneNumber,
            notes = notes
        )
}