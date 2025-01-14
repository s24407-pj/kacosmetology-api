package pl.kacosmetology.api.account

import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/accounts")
class AccountController(
    private val accountService: AccountService
) {
    @PostMapping
    fun createAccount(@Valid @RequestBody accountRequest: AccountRequest): ResponseEntity<URI> {
        val account = accountRequest.toModel()

        val id = accountService.createAccount(account)
        val uri = URI.create("/api/v1/accounts/$id")

        return ResponseEntity(uri, CREATED)
    }

    @GetMapping("/me")
    fun getMyAccount(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<AccountResponse> {
        val account = accountService.getByEmail(userDetails.username)
        val accountResponse = account.toResponse()
        return ResponseEntity(accountResponse, OK)
    }

    @GetMapping
    fun getAllAccounts(): ResponseEntity<List<AccountResponse>> {
        val accounts = accountService.getAllAccounts()
        val accountsResponse = accounts.map { it.toResponse() }
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

    fun Account.toResponse() =
        AccountResponse(
            id = id!!,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phoneNumber = phoneNumber,
            gender = gender
        )

}