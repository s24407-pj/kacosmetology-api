package pl.kacosmetology.api.auth.services

import org.springframework.stereotype.Service
import pl.kacosmetology.api.auth.models.BlackListToken
import pl.kacosmetology.api.auth.repositories.BlackListTokenRepository

@Service
class BlacklistTokenService(
    private val blackListTokenRepository: BlackListTokenRepository,
    private val tokenService: TokenService
) {
    fun addToBlacklist(token: String) {
        val expirationDate = tokenService.getExpirationDate(token)

        blackListTokenRepository.save(
            BlackListToken(
                token = token,
                ttl = expirationDate.time - System.currentTimeMillis() / 1000
            )
        )
    }

    fun isBlacklisted(token: String): Boolean {
        return blackListTokenRepository.existsById(token)
    }
}