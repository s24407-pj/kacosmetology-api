package pl.kacosmetology.api.reservation

import org.springframework.stereotype.Service
import pl.kacosmetology.api.exception.ResourceConflictException
import pl.kacosmetology.api.exception.ResourceNotFoundException
import java.time.LocalDate
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class ReservationService(val reservationRepository: ReservationRepository) {
    fun getAllReservations(
        fromDate: LocalDate,
        toDate: LocalDate
    ): List<Reservation> {
        return reservationRepository.findAllByAppointmentDateTimeBetween(
            fromDate.atStartOfDay(),
            toDate.atTime(23, 59, 59)
        )
    }

    fun getReservationById(id: UUID): Reservation = reservationRepository.findById(id)
        .getOrElse { throw ResourceNotFoundException("Nie znaleziono takiej rezerwacji.") }

    fun createReservation(reservation: Reservation):UUID {
        if (reservationRepository.existsReservationByAppointmentDateTimeBetween(
                reservation.appointmentDateTime,
                reservation.appointmentDateTime.plusMinutes(30) //TODO change to service duration
            )
        ) {
            throw ResourceConflictException("Rezerwacja w tym terminie już istnieje.")
        }

        reservationRepository.save(reservation)

        return reservation.id!!
    }

    fun updateReservation(id: UUID, reservation: Reservation) {
        val isReservation = reservationRepository.existsById(id)

        if (!isReservation) {
            throw ResourceNotFoundException("Nie znaleziono takiej rezerwacji.")
        }

        reservationRepository.save(reservation)
        //TODO
    }
}