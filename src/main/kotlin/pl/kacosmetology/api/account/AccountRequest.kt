package pl.kacosmetology.api.account

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class AccountRequest(
    @field:NotBlank(message = "Imię nie może być puste")
    @field:Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków")
    val firstName: String,

    @field:NotBlank(message = "Nazwisko nie może być puste")
    @field:Size(min = 2, max = 50, message = "Nazwisko musi mieć od 2 do 50 znaków")
    val lastName: String,

    @field:NotBlank(message = "Adres e-mail nie może być pusty")
    @field:Email(message = "Adres e-mail musi być poprawny")
    val email: String,

    @field:NotBlank(message = "Numer telefonu nie może być pusty")
    @field:Pattern(
        regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}\$",
        message = "Numer telefonu musi być poprawny (np. +123456789)"
    )
    val phoneNumber: String,

    val gender: Gender,

    @field:NotBlank(message = "Hasło nie może być puste")
    @field:Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,}\$",
        message = "Hasło musi zawierać co najmniej 8 znaków, jedną małą i dużą literę, jedną cyfre i jeden znak specjalny"
    )
    val password: String
)
