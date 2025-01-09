package pl.kacosmetology.api.auth.repositories

import org.springframework.data.repository.CrudRepository
import pl.kacosmetology.api.auth.models.BlackListToken

interface BlackListTokenRepository : CrudRepository<BlackListToken, String>