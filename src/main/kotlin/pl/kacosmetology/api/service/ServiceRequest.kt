package pl.kacosmetology.api.service

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class ServiceRequest(

    @field:NotBlank(message = "Nazwa usługi nie może być pusta")
    val name: String,

    @field:NotBlank(message = "Kategoria usługi nie może być pusta")
    val category: String,

    @field:NotBlank(message = "Opis usługi nie może być pusty")
    val description: String,

    @field:NotNull(message = "Cena usługi nie może być pusta")
    @field:Positive(message = "Cena usługi musi być większa od zera")
    val price: Int,

    @field:NotNull(message = "Czas trwania usługi nie może być pusty")
    @field:Positive(message = "Czas usługi musi być większy od zera")
    val duration: Int
)
