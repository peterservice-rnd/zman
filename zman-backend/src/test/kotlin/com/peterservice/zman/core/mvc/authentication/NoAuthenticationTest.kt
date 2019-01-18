package com.peterservice.zman.core.mvc.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.peterservice.zman.api.entities.ZServer
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.validation.constraints.NotNull

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = arrayOf("authentication.type: NONE", "spring.datasource.url: jdbc:h2:mem:zk"))
class NoAuthenticationTest {

    @Test
    fun getServersNoAuth() {
        mockMvc.perform(get("/api/servers"))
                .andExpect(status().isOk)
                .andReturn()
                .request
                .session
    }

    @Test
    fun addServersNoAuth() {
        val server = ZServer().also {
            it.alias = "alias"
            it.connectionString = "host:port"
        }
        mockMvc.perform(
            post("/api/servers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(server))
        )
            .andExpect(status().isCreated)
            .andReturn()
            .request
            .session
    }

    @Autowired
    @NotNull
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper
}