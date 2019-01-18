package com.peterservice.zman.core.mvc.authentication

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import javax.validation.constraints.NotNull


@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@PropertySource(factory = YamlPropertySourceFactory::class, value = "classpath:application-ldap.yml")
@TestPropertySource(properties = arrayOf("authentication.type: LDAP", "spring.datasource.url: jdbc:h2:mem:zk"))
class LdapAuthenticationViaUserBindTest {

    @Test
    fun positiveAuthentication() {
        val requestBuilder = SecurityMockMvcRequestBuilders.formLogin().user(testUserName).password(testUserPassword)
        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
    }

    @Test
    fun failWithNotAuthenticatedWithWrongUser() {
        val requestBuilder = SecurityMockMvcRequestBuilders.formLogin().user("this_user_doesnt_exist").password("1111")
        mockMvc.perform(requestBuilder)
                .andExpect(unauthenticated())
    }

    @Test
    fun failWithNotAuthenticatedWithWrongPassword() {
        val requestBuilder = SecurityMockMvcRequestBuilders.formLogin().user(testUserName).password(testUserPassword + "1111")
        mockMvc.perform(requestBuilder)
                .andExpect(unauthenticated())
    }

    @Test
    fun failWithNoAuthentication() {
        mockMvc.perform(get("/api/servers"))
                .andExpect(unauthenticated())
    }

    @Autowired
    @NotNull
    lateinit var mockMvc: MockMvc

    @Value("\${testUser.login}")
    val testUserName: String? = null

    @Value("\${testUser.password}")
    val testUserPassword: String? = null
}