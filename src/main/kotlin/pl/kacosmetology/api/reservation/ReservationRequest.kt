package pl.kacosmetology.api.reservation

import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class ReservationRequest(
    @field:NotBlank(message = "Adres e-mail nie może być pusty")
    @field:Email(message = "Adres e-mail musi być poprawny")
    val email: String,

    @field:NotBlank(message = "Imię nie może być puste")
    @field:Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków")
    val firstName: String,

    @field:NotBlank(message = "Nazwisko nie może być puste")
    @field:Size(min = 2, max = 50, message = "Nazwisko musi mieć od 2 do 50 znaków")
    val lastName: String,

    @field:NotBlank(message = "Numer telefonu nie może być pusty")
    @field:Pattern(
        regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}\$",
        message = "Numer telefonu musi być poprawny (np. +123456789)"
    )
    val phoneNumber: String,

    @field:NotNull(message = "Data wizyty nie może być pusta")
    @field:Future(message = "Data wizyty musi być w przyszłości")
    val appointmentDateTime: LocalDateTime,

    @field:NotNull(message = "Usługa nie może być pusta")
    val serviceId: Int,

    @field:Size(max = 500, message = "Notatki mogą zawierać maksymalnie 500 znaków")
    val notes: String?,
)