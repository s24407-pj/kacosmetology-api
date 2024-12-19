package pl.kacosmetology.api.reservation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pl.kacosmetology.api.AbstractTestContainers
import pl.kacosmetology.api.reservation.ReservationStatus.*
import java.time.LocalDateTime


@DataJpaTest
class ReservationRepositoryTest : AbstractTestContainers() {

    @Autowired
    lateinit var underTestRepository: ReservationRepository

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

    @Test
    fun findAllByAppointmentDateTimeBetween() {
        val reservations = listOf(
            reservation1, reservation2, reservation3, reservation4
        )

        underTestRepository.saveAll(reservations)

        val result = underTestRepository.findAllByAppointmentDateTimeBetween(
            LocalDateTime.of(2024, 5, 1, 10, 0),
            LocalDateTime.of(2024, 6, 1, 23, 59)
        )

        assertEquals(2, result.size)
    }

    @Test
    fun existsReservationByAppointmentDateTimeBetween() {
        val reservations = listOf(
            reservation1, reservation2, reservation3, reservation4
        )

        underTestRepository.saveAll(reservations)

        val result = underTestRepository.existsReservationByAppointmentDateTimeBetween(
            LocalDateTime.of(2024, 5, 1, 10, 0),
            LocalDateTime.of(2024, 6, 1, 23, 59)
        )

        assertEquals(true, result)
    }

    @Test
    fun notExistsReservationByAppointmentDateTimeBetween() {
        val reservations = listOf(
            reservation1, reservation2, reservation3, reservation4
        )

        underTestRepository.saveAll(reservations)

        val result = underTestRepository.existsReservationByAppointmentDateTimeBetween(
            LocalDateTime.of(2024, 7, 1, 10, 0),
            LocalDateTime.of(2024, 8, 1, 23, 59)
        )

        assertEquals(false, result)
    }
}