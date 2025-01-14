package pl.kacosmetology.api.auth.services

import org.springframework.stereotype.Service
import pl.kacosmetology.api.auth.models.BlackListToken
import pl.kacosmetology.api.auth.repositories.BlacklistTokenRepository

@Service
class BlacklistTokenService(
    private val blacklistTokenRepository: BlacklistTokenRepository,
    private val tokenService: TokenService
) {
    fun addToBlacklist(token: String) {
        val expirationDate = tokenService.getExpirationDate(token)

        blacklistTokenRepository.save(
            BlackListToken(
                token = token,
                ttl = expirationDate.time - System.currentTimeMillis() / 1000
            )
        )
    }

    fun isBlacklisted(token: String): Boolean {
        return blacklistTokenRepository.existsById(token)
    }
}