package com.peterservice.zman.core.mvc.authentication

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.validation.constraints.NotNull

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = arrayOf("authentication.type: NONE"))
class NoAuthenticationTest {

    @Test
    fun getServicesInfoPositiveWithBasicAuthentication() {
        mockMvc.perform(get("/api/servers"))
                .andExpect(status().isOk)
                .andReturn()
                .request
                .session
    }

    @Autowired
    @NotNull
    lateinit var mockMvc: MockMvc
}