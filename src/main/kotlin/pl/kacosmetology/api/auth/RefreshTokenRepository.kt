package pl.kacosmetology.api.auth

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.kacosmetology.api.auth.models.RefreshToken

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, String>