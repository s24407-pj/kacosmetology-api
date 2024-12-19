package pl.kacosmetology.api.reservation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface ReservationRepository : JpaRepository<Reservation, UUID> {
    fun findAllByAppointmentDateTimeBetween(
        dateAfter: LocalDateTime,
        dateBefore: LocalDateTime
    ): List<Reservation>

    fun existsReservationByAppointmentDateTimeBetween(
        dateAfter: LocalDateTime,
        dateBefore: LocalDateTime
    ): Boolean
}