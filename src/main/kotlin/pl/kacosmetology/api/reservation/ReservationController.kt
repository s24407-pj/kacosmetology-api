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

        return ResponseEntity(reservationsResponse,OK)
    }


    @GetMapping("/{id}")
    fun getReservationById(@PathVariable id: UUID): ResponseEntity<ReservationResponse> {
        val reservationResponse = reservationService.getReservationById(id).toResponse()

        return ResponseEntity(reservationResponse,OK)
    }


    @PostMapping("/")
    fun createReservation(@RequestBody reservationRequest: ReservationRequest): ResponseEntity<UUID> {
        val reservation = reservationRequest.toModel()

        val id = reservationService.createReservation(reservation)

        return ResponseEntity(id,CREATED)
    }

    @PostMapping("/{id}")
    fun updateReservation(@PathVariable id: UUID, reservationRequest: ReservationRequest): ResponseEntity<Unit> {
        val reservation = reservationRequest.toModel()

        reservationService.updateReservation(id, reservation)

        return ResponseEntity(OK)
    }

}