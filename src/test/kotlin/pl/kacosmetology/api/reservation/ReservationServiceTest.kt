package pl.kacosmetology.api.reservation

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.kacosmetology.api.reservation.ReservationStatus.*
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class ReservationServiceTest {
    private val reservation1 = Reservation(
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
    fun getAllReservations() {
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
    fun getReservationById() {
    }

    @Test
    fun createReservation() {
    }

    @Test
    fun updateReservation() {
    }

    @Test
    fun getReservationRepository() {
    }
}