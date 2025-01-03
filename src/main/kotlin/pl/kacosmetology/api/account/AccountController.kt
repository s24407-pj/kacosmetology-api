package pl.kacosmetology.api.account

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/accounts")
class AccountController(private val accountService: AccountService) {

    @PostMapping
    fun createAccount(@RequestBody accountRequest: AccountRequest): ResponseEntity<UUID> {
        val account = accountRequest.toModel()

        val id = accountService.createAccount(account)

        return ResponseEntity(id, CREATED)
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
            password = password
        )

}