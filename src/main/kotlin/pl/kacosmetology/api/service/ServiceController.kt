package pl.kacosmetology.api.service

import jakarta.validation.Valid
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.URI

@Controller
@RequestMapping("/api/v1/services")
class ServiceController(
    private val serviceService: ServiceService
) {
    @GetMapping
    fun getServices(): ResponseEntity<List<ServiceResponse>> {
        val servicesResponse = serviceService.getAllServices()
            .map { it.toResponse() }

        return ResponseEntity(servicesResponse, OK)
    }

    @PostMapping
    fun addService(
        @Valid @RequestBody serviceRequest: ServiceRequest
    ): ResponseEntity<URI> {
        val service = serviceRequest.toModel()

        val id = serviceService.addService(service)
        val uri = URI.create("/api/v1/services/$id")

        return ResponseEntity(uri, CREATED)
    }


    @PatchMapping("/{id}")
    fun updateService(
        @Valid @RequestBody serviceUpdateRequest: ServiceUpdateRequest,
        @PathVariable id: Long
    ): ResponseEntity<Unit> {

        serviceService.updateService(serviceUpdateRequest, id)

        return ResponseEntity(NO_CONTENT)
    }

    @PatchMapping("/{id}/availability")
    fun updateServiceAvailability(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {

        serviceService.updateServiceAvailability(id)

        return ResponseEntity(NO_CONTENT)
    }

    private fun Service.toResponse() =
        ServiceResponse(
            id = id!!,
            name = name,
            category = category,
            description = description,
            price = price,
            duration = duration
        )

    private fun ServiceRequest.toModel() =
        Service(
            name = name,
            category = category,
            description = description,
            price = price,
            duration = duration
        )
}


