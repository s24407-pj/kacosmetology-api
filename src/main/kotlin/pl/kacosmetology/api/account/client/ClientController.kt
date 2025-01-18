package pl.kacosmetology.api.account.client

import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/clients")
class ClientController(
    private val clientService: ClientService
) {
    @PostMapping
    fun createAccount(@Valid @RequestBody clientRequest: ClientRequest): ResponseEntity<URI> {
        val account = clientRequest.toModel()

        val id = clientService.createAccount(account)
        val uri = URI.create("/api/v1/clients/$id")

        return ResponseEntity(uri, CREATED)
    }

    @GetMapping("/me")
    fun getMyClientAccount(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<ClientResponse> {
        val account = clientService.getByEmail(userDetails.username)
        val accountResponse = account.toResponse()
        return ResponseEntity(accountResponse, OK)
    }

    @GetMapping
    fun getAllClients(): ResponseEntity<List<ClientResponse>> {
        val accounts = clientService.getAllAccounts()
        val accountsResponse = accounts.map { it.toResponse() }
        return ResponseEntity(accountsResponse, OK)
    }

    private fun ClientRequest.toModel() =
        Client(

            firstName = firstName,
            lastName = lastName,
            email = email,
            phoneNumber = phoneNumber,
            accountPassword = password,
            gender = gender
        )

    fun Client.toResponse() =
        ClientResponse(
            id = id!!,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phoneNumber = phoneNumber,
            gender = gender
        )

}