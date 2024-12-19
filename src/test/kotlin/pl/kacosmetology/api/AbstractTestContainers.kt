package pl.kacosmetology.api

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
abstract class AbstractTestContainers {
    companion object {
        @Container
        val postgresContainer = PostgreSQLContainer("postgres:17-alpine").apply {
            withDatabaseName("test")
            withUsername("test")
            withPassword("test")
        }

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.apply {
                add("spring.datasource.url", postgresContainer::getJdbcUrl)
                add("spring.datasource.username", postgresContainer::getUsername)
                add("spring.datasource.password", postgresContainer::getPassword)
            }
        }
    }
}