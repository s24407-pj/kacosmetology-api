package pl.kacosmetology.api.auth.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.kacosmetology.api.auth.models.RefreshToken
import java.util.*

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, String> {
    override fun findById(token: String): Optional<RefreshToken>
}