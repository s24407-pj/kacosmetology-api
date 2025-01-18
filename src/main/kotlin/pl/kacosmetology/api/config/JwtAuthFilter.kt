package pl.kacosmetology.api.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import pl.kacosmetology.api.auth.models.TokenType
import pl.kacosmetology.api.auth.services.CustomUserDetailsService
import pl.kacosmetology.api.auth.services.TokenService

@Component
class JwtAuthFilter(
    private val userDetailsService: CustomUserDetailsService,

    private val tokenService: TokenService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader("Authorization")

        if (!authHeader.doesNotContainBearer()) {
            try {
                val jwtToken = authHeader!!.extractTokenValue()
                val email = tokenService.extractEmail(jwtToken)

                if (SecurityContextHolder.getContext().authentication == null && email!=null){
                    val userDetails: UserDetails? = userDetailsService.loadUserByUsername(email)

                    if(userDetails?.username == email){
                        updateSecurityContext(userDetails, request)
                    }
                }
            } catch (e: Exception) {
                response.writer.write(
                    """{"error": "Filter Authorization error: 
                    |${e.message ?: "unknown error"}"}""".trimMargin()
                )
            }
        }


        filterChain.doFilter(request, response)
    }

    private fun updateSecurityContext(foundAccount: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(foundAccount, null, foundAccount.authorities)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = authToken
    }

    private fun isAccessToken(token: String): Boolean = tokenService.extractTokenType(token) == TokenType.ACCESS

    private fun String?.doesNotContainBearer(): Boolean = this == null || !this.startsWith("Bearer ")

    private fun String.extractTokenValue(): String = this.substringAfter("Bearer ")
}