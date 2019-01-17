package com.peterservice.zman.core.mvc.authentication

import com.peterservice.zman.core.zookeeper.YamlPropertySourceFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.validation.constraints.NotNull

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@PropertySource(factory = YamlPropertySourceFactory::class, value = "classpath:application-ldap-search.yml")
@TestPropertySource(properties = arrayOf("authentication.type: LDAP"))
class LdapAuthenticationViaUserSearchITTest {

    @Test
    @Throws(Exception::class)
    fun getServicesInfoPositiveWithBasicAuthentication() {

        mockMvc.perform(get("/api/servers")
                .with(httpBasic(testUserName, testUserPassword)))
                .andExpect(status().isOk)
                .andReturn()
                .request
                .session
    }

    @Test
    @Throws(Exception::class)
    fun failWithNotAuthenticatedWithWrongUser() {
        mockMvc.perform(get("/api/servers")
                .with(httpBasic("this user doesn't exist", "1111")))
                .andExpect(unauthenticated())
                .andExpect(status().isUnauthorized())
    }

    @Test
    @Throws(Exception::class)
    fun failWithNotAuthenticatedWithWrongPassword() {
        mockMvc.perform(get("/api/servers")
                .with(httpBasic(testUserName, testUserPassword + "1111")))
                .andExpect(unauthenticated())
                .andExpect(status().isUnauthorized())
    }

    @Test
    @Throws(Exception::class)
    fun failWithNoAuthentication() {
        mockMvc.perform(get("/api/servers"))
                .andExpect(unauthenticated())
                .andExpect(status().isUnauthorized())
    }

    @Autowired
    @NotNull
    lateinit var mockMvc: MockMvc

    @Value("\${testUser.login}")
    val testUserName: String? = null

    @Value("\${testUser.password}")
    val testUserPassword: String? = null
}