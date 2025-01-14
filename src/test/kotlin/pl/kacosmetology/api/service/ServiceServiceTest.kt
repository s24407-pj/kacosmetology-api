package pl.kacosmetology.api.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.kacosmetology.api.exception.ResourceNotFoundException
import java.util.*

@ExtendWith(MockKExtension::class)
class ServiceServiceTest {
    @MockK
    lateinit var serviceRepositoryMock: ServiceRepository

    @InjectMockKs
    lateinit var underTest: ServiceService

    private val service1 = Service(
        id = 1,
        name = "Service1",
        category = "Category1",
        description = "Description1",
        price = 100,
        duration = 60,
    )

    @Test
    fun `should get all services`() {
        // given
        every { serviceRepositoryMock.findAll() } returns listOf(service1)

        // when
        val result = underTest.getAllServices()

        // then
        assertEquals(listOf(service1), result)
    }

    @Test
    fun `should add service`() {
        // given
        every { serviceRepositoryMock.save(service1) } returns service1

        // when
        val result = underTest.addService(service1)

        // then
        assertEquals(result, service1.id)
    }

    @Test
    fun `should update service`() {
        // given
        val updateRequest = ServiceUpdateRequest(
            price = 999
        )
        every { serviceRepositoryMock.findById(1) } returns Optional.of(service1)
        every {
            serviceRepositoryMock.save(any())
        } returns service1

        // when
        underTest.updateService(updateRequest, 1)

        // then
        verify {
            serviceRepositoryMock.save(
                service1.copy(
                    price = updateRequest.price!!
                )
            )
        }
    }

    @Test
    fun `should throw when service not found by id when update`() {
        // given
        every { serviceRepositoryMock.findById(1) } returns Optional.empty()

        // when
        try {
            underTest.updateService(ServiceUpdateRequest(), 1)
        } catch (exception: ResourceNotFoundException) {
            // then
            assert(true)
        }
    }

    @Test
    fun `should update service availability`() {
        // given
        every { serviceRepositoryMock.findById(1) } returns Optional.of(service1)
        every {
            serviceRepositoryMock.save(any())
        } returns service1

        // when
        underTest.updateServiceAvailability(1)

        // then
        verify {
            serviceRepositoryMock.save(
                service1.copy(
                    isAvailable = !service1.isAvailable!!
                )
            )
        }
    }

    @Test
    fun `should throw when service not found by id when update availability`() {
        // given
        every { serviceRepositoryMock.findById(1) } returns Optional.empty()

        // when
        try {
            underTest.updateServiceAvailability(1)
        } catch (exception: ResourceNotFoundException) {
            // then
            assert(true)
        }
    }
}