package pl.kacosmetology.api.reservation

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import pl.kacosmetology.api.exception.ResourceConflictException
import pl.kacosmetology.api.exception.ResourceNotFoundException
import pl.kacosmetology.api.reservation.ReservationStatus.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class ReservationServiceTest {
    private val reservation1 = Reservation(
        id = UUID.randomUUID(),
        appointmentDateTime = LocalDateTime.of(2024, 1, 1, 10, 0),
        firstName = "Janusz",
        lastName = "Kowalski",
        serviceId = 1,
        email = "jkowalski@email.com",
        phoneNumber = "792743893",
        status = CANCELLED,
        notes = "Some notes",
    )
    private val reservation2 = Reservation(
        id = UUID.randomUUID(),
        appointmentDateTime = LocalDateTime.of(2024, 5, 1, 10, 0),
        firstName = "Janusz",
        lastName = "Kowalski",
        serviceId = 1,
        email = "jkowalski@email.com",
        phoneNumber = "792743893",
        status = CONFIRMED,
        notes = "Some notes",
    )

    private val reservation3 = Reservation(
        id = UUID.randomUUID(),
        appointmentDateTime = LocalDateTime.of(2024, 6, 1, 10, 0),
        firstName = "Janusz",
        lastName = "Kowalski",
        serviceId = 1,
        email = "jkowalski@email.com",
        phoneNumber = "792743893",
        status = CONFIRMED,
        notes = "Some notes",
    )
    private val reservation4 = Reservation(
        id = UUID.randomUUID(),
        appointmentDateTime = LocalDateTime.of(2024, 12, 1, 10, 0),
        firstName = "Janusz",
        lastName = "Kowalski",
        serviceId = 1,
        email = "jkowalski@email.com",
        phoneNumber = "792743893",
        status = PENDING,
        notes = "Some notes",
    )

    @MockK
    lateinit var mockRepository: ReservationRepository

    @InjectMockKs
    lateinit var underTestService: ReservationService

    @Test
    fun `should return all reservations`() {
        // Given
        every { mockRepository.findAllByAppointmentDateTimeBetween(any(), any()) } returns listOf(
            reservation1,
            reservation2,
            reservation3,
            reservation4
        )
        val dateFrom = LocalDate.of(2024, 1, 1)
        val dateTo = LocalDate.of(2024, 12, 1)

        // When
        val result = underTestService.getAllReservations(
            dateFrom,
            dateTo
        )

        // Then
        assert(result.size == 4)
        assert(result.contains(reservation1))
        assert(result.contains(reservation4))
        verify {
            mockRepository.findAllByAppointmentDateTimeBetween(
                dateFrom.atStartOfDay(),
                dateTo.atTime(23, 59, 59)
            )
        }

    }

    @Test
    fun `should return reservation by id`() {
        // Given
        val id = reservation1.id!!
        every { mockRepository.findById(id) } returns Optional.of(reservation1)

        // When
        val result = underTestService.getReservationById(id)

        // Then
        assertEquals(result, reservation1)
    }

    @Test
    fun `should throw when reservation not found by id`() {
        // Given
        val id = UUID.randomUUID()
        every { mockRepository.findById(id) } returns Optional.empty()

        // When
        val exception = assertThrows<ResourceNotFoundException> {
            underTestService.getReservationById(id)
        }

        // Then
        assertEquals(exception.message, "Nie znaleziono takiej rezerwacji.")
    }

    @Test
    fun `should create reservation`() {
        // Given
        val reservation = reservation1
        every { mockRepository.save(reservation) } returns reservation
        every { mockRepository.existsReservationByAppointmentDateTimeBetween(any(), any()) } returns false

        // When
        val result = underTestService.createReservation(reservation)

        // Then
        assertEquals(result, reservation.id)
        verify { mockRepository.save(reservation) }
    }

    @Test
    fun `should throw when reservation has time conflict`() {
        // Given
        val reservation = reservation1
        every { mockRepository.existsReservationByAppointmentDateTimeBetween(any(), any()) } returns true

        // When
        val exception = assertThrows<ResourceConflictException> {
            underTestService.createReservation(reservation)
        }

        // Then
        assertEquals(exception.message, "Rezerwacja w tym terminie już istnieje.")
    }

    @Test
    fun updateReservation() {
        //TODO
    }
}