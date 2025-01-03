package pl.kacosmetology.api.reservation

import java.time.LocalDateTime

data class ReservationRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val appointmentDateTime: LocalDateTime,
    val serviceId: Int,
    val notes: String?,

    ) {

}