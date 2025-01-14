package pl.kacosmetology.api.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import pl.kacosmetology.api.exception.ResourceNotFoundException
import org.springframework.stereotype.Service as ServiceComponent


@ServiceComponent
class ServiceService(
    private val serviceRepository: ServiceRepository
) {
    @Cacheable("services")
    fun getAllServices(): List<Service> {
        val services = serviceRepository.findAll()
        return services
    }

    @CacheEvict("services", key = "#id")
    fun updateService(updateRequest: ServiceUpdateRequest, id: Long) {
        val service = serviceRepository.findById(id).orElseThrow {
            throw ResourceNotFoundException("Serwis o takim ID nie istnieje.")
        }
        //TODO add validation
        val updatedService = service.copy(
            name = updateRequest.name ?: service.name,
            description = updateRequest.description ?: service.description,
            price = updateRequest.price ?: service.price,
            duration = updateRequest.duration ?: service.duration,
            category = updateRequest.category ?: service.category,
        )


        serviceRepository.save(updatedService)
    }

    @CacheEvict("services", key = "#id")
    fun updateServiceAvailability(id: Long) {

        val service = serviceRepository.findById(id).orElseThrow {
            throw ResourceNotFoundException("Serwis o takim ID nie istnieje.")
        }

        val updatedService = service.copy(
            isAvailable = !service.isAvailable!!
        )


        serviceRepository.save(updatedService)
    }

    fun addService(service: Service): Long {
        return serviceRepository.save(service).id!!
    }
}