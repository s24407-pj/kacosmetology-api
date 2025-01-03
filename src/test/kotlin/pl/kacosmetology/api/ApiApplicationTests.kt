package pl.kacosmetology.api

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.annotation.EnableCaching

@SpringBootTest
@EnableCaching
class ApiApplicationTests : AbstractTestContainers() {

    @Test
    fun contextLoads() {
    }

}
