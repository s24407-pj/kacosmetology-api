package pl.kacosmetology.api.reservation

import jakarta.validation.Valid
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.kacosmetology.api.reservation.ReservationStatus.PENDING
import java.net.URI
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("api/v1/reservations")
class ReservationController(
    private val reservationService: ReservationService
) {
    @GetMapping
    fun getAllReservations(
        @RequestParam fromDate: LocalDate,
        @RequestParam toDate: LocalDate
    ): ResponseEntity<List<ReservationResponse>> {
        val reservations = reservationService.getAllReservations(fromDate, toDate)

        val reservationsResponse = reservations.map { it.toResponse() }

        return ResponseEntity(reservationsResponse, OK)
    }


    @GetMapping("/{id}")
    fun getReservationById(@PathVariable id: UUID): ResponseEntity<ReservationResponse> {
        val reservation = reservationService.getReservationById(id)

        val reservationResponse = reservation.toResponse()

        return ResponseEntity(reservationResponse, OK)
    }


    @PostMapping
    fun createReservation(@Valid @RequestBody reservationRequest: ReservationRequest): ResponseEntity<URI> {
        val reservation = reservationRequest.toModel()

        val id = reservationService.createReservation(reservation)
        val uri = URI.create("/api/v1/reservations/$id")

        return ResponseEntity(uri, CREATED)
    }

    @PatchMapping("/{id}")
    fun updateReservationStatus(
        @PathVariable id: UUID,
        @RequestParam status: ReservationStatus
    ): ResponseEntity<Unit> {
        reservationService.updateReservationStatus(id, status)

        return ResponseEntity(NO_CONTENT)
    }

    private fun Reservation.toResponse() =
        ReservationResponse(
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

    private fun ReservationRequest.toModel() =
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