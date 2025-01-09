package pl.kacosmetology.api.account

import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/accounts")
class AccountController(private val accountService: AccountService) {

    @PostMapping
    fun createAccount(@Valid @RequestBody accountRequest: AccountRequest): ResponseEntity<UUID> {
        val account = accountRequest.toModel()

        val id = accountService.createAccount(account)

        return ResponseEntity(id, CREATED)
    }

    @GetMapping("/me")
    fun getMyAccount(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<AccountResponse> {
        val account = accountService.getByEmail(userDetails.username)

        return ResponseEntity(account.toResponse(), OK)
    }

    @GetMapping
    fun getAllAccounts(): ResponseEntity<List<AccountResponse>> {
        val accountsResponse = accountService.getAllAccounts().map { it.toResponse() }

        return ResponseEntity(accountsResponse, OK)
    }

    private fun AccountRequest.toModel() =
        Account(
            firstName = firstName,
            lastName = lastName,
            email = email,
            phoneNumber = phoneNumber,
            password = password,
            gender = gender
        )

}