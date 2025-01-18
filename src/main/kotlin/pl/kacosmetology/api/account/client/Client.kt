package pl.kacosmetology.api.account.client

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.hibernate.proxy.HibernateProxy
import pl.kacosmetology.api.account.Account
import pl.kacosmetology.api.account.Role
import pl.kacosmetology.api.account.Role.ROLE_USER

@Entity
data class Client(
    override val id: Long? = null,
    override val firstName: String,
    override val lastName: String,
    override val email: String,
    override val phoneNumber: String,
    override val accountPassword: String,

    @Enumerated(EnumType.STRING)
    val gender: Gender,

    @Enumerated(EnumType.STRING)
    override val role: Role = ROLE_USER,
) : Account(
    null,
    firstName,
    lastName,
    email,
    phoneNumber,
    accountPassword,
    role = ROLE_USER
) {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as Client

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(  id = $id   ,   version = $version )"
    }
}
