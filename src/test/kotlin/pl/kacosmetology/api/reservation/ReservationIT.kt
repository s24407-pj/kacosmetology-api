//package pl.kacosmetology.api.reservation
//
//import org.junit.jupiter.api.AfterEach
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import org.testcontainers.junit.jupiter.Testcontainers
//import pl.kacosmetology.api.AbstractTestContainers
//import java.time.LocalDate
//import kotlin.test.Test
//
//@SpringBootTest(webEnvironment = RANDOM_PORT)
//@AutoConfigureMockMvc
//@Testcontainers
//class ReservationIT : AbstractTestContainers() {
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    @AfterEach
//    fun tearDown() {
//        // Optional cleanup logic after each test
//    }
//    //TODO security
//    @Test
//    fun `should fetch all reservations`() {
//        // Assuming the database is preloaded with test data
//        mockMvc.perform(
//            get("/api/v1/reservations")
//                .param("fromDate", LocalDate.now().minusDays(30).toString())
//                .param("toDate", LocalDate.now().toString())
//        )
//            .andExpect(status().isOk)
//            .andExpect(jsonPath("$.length()").value(0)) // Update based on expected data
//    }
//
////    @Test
////    fun `should create a reservation`() {
////        val requestJson = """
////            {
////                "name": "Test Reservation",
////                "date": "${LocalDate.now()}",
////                "status": "PENDING"
////            }
////        """.trimIndent()
////
////        mockMvc.perform(
////            post("/api/v1/reservations/")
////                .contentType("application/json")
////                .content(requestJson)
////        )
////            .andExpect(status().isCreated)
////            .andExpect(jsonPath("$").isNotEmpty)
////    }
////
////    @Test
////    fun `should fetch reservation by ID`() {
////        val reservationId = UUID.randomUUID() // Use a valid ID from preloaded test data
////
////        mockMvc.perform(
////            get("/api/v1/reservations/{id}", reservationId)
////        )
////            .andExpect(status().isOk)
////            .andExpect(jsonPath("$.name").value("Test Reservation")) // Update based on expected data
////    }
////
////    @Test
////    fun `should update reservation status`() {
////        val reservationId = UUID.randomUUID() // Use a valid ID from preloaded test data
////
////        mockMvc.perform(
////            post("/api/v1/reservations/{id}", reservationId)
////                .param("status", "CONFIRMED")
////        )
////            .andExpect(status().isOk)
////    }
//}