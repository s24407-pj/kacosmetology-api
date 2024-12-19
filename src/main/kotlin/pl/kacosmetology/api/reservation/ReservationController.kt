package pl.kacosmetology.api.reservation

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("api/v1/reservations")
class ReservationController(val reservationService: ReservationService) {

    @GetMapping("/")
    fun getAllReservations(
        @RequestParam fromDate: LocalDate,
        @RequestParam toDate: LocalDate
    ): ResponseEntity<List<ReservationResponse>> {
        val reservationsResponse = reservationService.getAllReservations(fromDate, toDate).map { it.toResponse() }

        return ResponseEntity.ok(reservationsResponse)
    }


    @GetMapping("/{id}")
    fun getReservationById(@PathVariable id: UUID): ResponseEntity<ReservationResponse> {
        val reservationResponse = reservationService.getReservationById(id).toResponse()

        return ResponseEntity.ok(reservationResponse)
    }


    @PostMapping("/")
    fun createReservation(reservationRequest: ReservationRequest): ResponseEntity<Unit> {
        val reservation = reservationRequest.toModel()

        reservationService.createReservation(reservation)

        return ResponseEntity(CREATED)
    }

    @PostMapping("/{id}")
    fun updateReservation(@PathVariable id: UUID, reservationRequest: ReservationRequest): ResponseEntity<Unit> {
        val reservation = reservationRequest.toModel()

        reservationService.updateReservation(id, reservation)

        return ResponseEntity(OK)
    }

}