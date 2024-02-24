//package com.siso.web.controller
//
//import io.micronaut.core.type.Argument
//import io.micronaut.http.HttpRequest
//import io.micronaut.http.HttpStatus
//import io.micronaut.http.MutableHttpRequest
//import io.micronaut.http.client.HttpClient
//import io.micronaut.http.client.annotation.Client
//import io.micronaut.http.client.exceptions.HttpClientResponseException
//import io.micronaut.test.extensions.junit5.annotation.MicronautTest
//import jakarta.inject.Inject
//import org.flywaydb.core.Flyway
//import org.junit.jupiter.api.AfterAll
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertNotNull
//import org.junit.jupiter.api.BeforeAll
//import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
//import org.junit.jupiter.api.Order
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//import org.junit.jupiter.api.TestMethodOrder
//import org.junit.jupiter.api.assertThrows
//
//@MicronautTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(OrderAnnotation::class)
//class UserControllerTest {
//
//    @Inject
//    @field:Client("/")
//    lateinit var httpClient: HttpClient
//
//    @Inject
//    lateinit var flyway: Flyway
//
//    private lateinit var accessToken: String
//    private var userId: Long = 0
//
//    @BeforeAll
//    fun setUp(){
//        flyway.clean()
//        flyway.migrate()
//    }
//
//    @AfterAll
//    fun clean(){
//        flyway.clean()
//    }
//
//    @Test
//    @Order(1)
//    fun `should create user success when receive valid body and valid admin user token`(){
//        val requestBody = "{\"email\": \"teste@email.com\", \"password\": \"12345678\"}"
//        val request: MutableHttpRequest<String>? = HttpRequest.POST("/users", requestBody).bearerAuth(accessToken)
//        val response = httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
//
//        assertNotNull(response)
//        val userResponse = response.body.get().first() as UserResponse
//        assertNotNull(userResponse)
//        assertNotNull(userResponse.id)
//        userId = userResponse.id
//        assertNotNull(userResponse.createdAt)
//        assertNotNull(userResponse.updatedAt)
//        assertEquals("teste@email.com", userResponse.email)
//        assertEquals(HttpStatus.CREATED, response.status)
//    }
//
//    @Test
//    @Order(2)
//    fun `should create user error when try create another user with same email`(){
//        val requestBody = "{\"email\": \"teste@email.com\", \"password\": \"12345678\"}"
//        val request: MutableHttpRequest<String>? = HttpRequest.POST("/users", requestBody).bearerAuth(accessToken)
//
//        val thrown = assertThrows<HttpClientResponseException> {
//            httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
//        }
//
//        assertEquals("Usuário já existente com e-mail: teste@email.com.", thrown.message)
//        assertEquals(HttpStatus.CONFLICT, thrown.status)
//    }
//
//    @Test
//    @Order(3)
//    fun `should return unauthorized when try create user without admin user token`(){
//        val requestBody = "{\"email\": \"teste@email.com\", \"password\": \"12345678\"}"
//        val request: MutableHttpRequest<String>? =
//            HttpRequest.POST("/users", requestBody)
//
//        val thrown = assertThrows<HttpClientResponseException> {
//            httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
//        }
//
//        assertEquals(HttpStatus.UNAUTHORIZED, thrown.status)
//    }
//
//    @Test
//    @Order(4)
//    fun `should return bad request when create user with invalid size of password`(){
//        val requestBody = "{\"email\": \"teste@email.com\", \"password\": \"1234567\"}"
//        val request: MutableHttpRequest<String>? =
//            HttpRequest.POST("/users", requestBody).header("Authorization", "Bearer $accessToken")
//
//        val thrown = assertThrows<HttpClientResponseException> {
//            httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
//        }
//
//        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
//    }
//
//    @Test
//    @Order(5)
//    fun `should return bad request when create user with invalid email`(){
//        val requestBody = "{\"email\": \"teste\", \"password\": \"12345678\"}"
//        val request: MutableHttpRequest<String>? =
//            HttpRequest.POST("/users", requestBody).header("Authorization", "Bearer $accessToken")
//
//        val thrown = assertThrows<HttpClientResponseException> {
//            httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
//        }
//
//        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
//    }
//
//
//    @Test
//    @Order(6)
//    fun `should update user password success when receive valid body and valid admin user token`(){
//        val requestBody = "{\"old_password\": \"Mudar@123\", \"new_password\": \"Teste1234\", \"new_password_confirmation\": \"Teste1234\"}"
//        val request: MutableHttpRequest<String>? = HttpRequest.PATCH("/users/password", requestBody).bearerAuth(accessToken)
//        val response = httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
//
//        assertEquals(HttpStatus.OK, response.status)
//    }
//
//    @Test
//    @Order(7)
//    fun `should update user password fail when receive invalid body with different passwords and valid admin user token`(){
//        val requestBody = "{\"old_password\": \"Teste1234\", \"new_password\": \"Mudar@1234\", \"new_password_confirmation\": \"1234@Mudar\"}"
//        val request: MutableHttpRequest<String>? = HttpRequest.PATCH("/users/password", requestBody).bearerAuth(accessToken)
//
//
//        val thrown = assertThrows<HttpClientResponseException> {
//            httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
//        }
//
//        assertEquals("Novas senhas não são iguais.", thrown.message)
//        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
//    }
//
//    @Test
//    @Order(8)
//    fun `should update user password fail when receive invalid body with different actual password and valid admin user token`(){
//        val requestBody = "{\"old_password\": \"Teste123456\", \"new_password\": \"Mudar@1234\", \"new_password_confirmation\": \"Mudar@1234\"}"
//        val request: MutableHttpRequest<String>? = HttpRequest.PATCH("/users/password", requestBody).bearerAuth(accessToken)
//
//
//        val thrown = assertThrows<HttpClientResponseException> {
//            httpClient.toBlocking().exchange(request, Argument.listOf(UserResponse::class.java))
//        }
//
//        assertEquals("Senha atual não confere.", thrown.message)
//        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
//    }
//
//
//
//
//
//}