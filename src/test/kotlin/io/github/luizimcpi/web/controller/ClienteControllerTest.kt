package io.github.luizimcpi.web.controller

import io.github.luizimcpi.web.dto.response.ExtratoResponse
import io.github.luizimcpi.web.dto.response.TransacaoResponse
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertThrows

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation::class)
class UserControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var flyway: Flyway

    @BeforeAll
    fun setUp(){
        flyway.clean()
        flyway.migrate()
    }

    @AfterAll
    fun clean(){
        flyway.clean()
    }

    @Test
    @Order(1)
    fun `should create transaction success when receive valid body with type debit`(){
        val requestBody = "{\"valor\": 100000, \"tipo\": \"d\", \"descricao\": \"teste\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)
        val response = httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))

        assertNotNull(response)
        val transacaoResponse = response.body.get().first() as TransacaoResponse
        assertNotNull(transacaoResponse)
        assertNotNull(transacaoResponse.saldo)
        assertNotNull(transacaoResponse.limite)
        assertEquals(-100000, transacaoResponse.saldo)
        assertEquals(100000, transacaoResponse.limite)
        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    @Order(2)
    fun `should create transaction success when receive valid body with type credit`(){
        val requestBody = "{\"valor\": 100000, \"tipo\": \"c\", \"descricao\": \"teste\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)
        val response = httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))

        assertNotNull(response)
        val transacaoResponse = response.body.get().first() as TransacaoResponse
        assertNotNull(transacaoResponse)
        assertNotNull(transacaoResponse.saldo)
        assertNotNull(transacaoResponse.limite)
        assertEquals(0, transacaoResponse.saldo)
        assertEquals(100000, transacaoResponse.limite)
        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    @Order(3)
    fun `should create transaction fail when receive valid body with type debit and value more than limit`(){
        val requestBody = "{\"valor\": 100001, \"tipo\": \"d\", \"descricao\": \"teste\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.status)
    }

    @Test
    @Order(4)
    fun `should create transaction fail when receive invalid body without value`(){
        val requestBody = "{\"tipo\": \"d\", \"descricao\": \"teste\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.status)
    }

    @Test
    @Order(5)
    fun `should create transaction fail when receive invalid body with zero value`(){
        val requestBody = "{\"valor\": 0, \"tipo\": \"c\", \"descricao\": \"teste\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.status)
    }

    @Test
    @Order(6)
    fun `should create transaction fail when receive invalid body with decimal value`(){
        val requestBody = "{\"valor\": 1.2, \"tipo\": \"c\", \"descricao\": \"teste\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.status)
    }

    @Test
    @Order(7)
    fun `should create transaction fail when receive invalid body without tipo`(){
        val requestBody = "{\"valor\": 100000, \"descricao\": \"teste\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.status)
    }

    @Test
    @Order(8)
    fun `should create transaction fail when receive invalid body with empty tipo`(){
        val requestBody = "{\"valor\": 100000, \"tipo\": \"\", \"descricao\": \"teste\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.status)
    }


    @Test
    @Order(9)
    fun `should create transaction fail when receive invalid body with tipo z`(){
        val requestBody = "{\"valor\": 100000, \"tipo\": \"z\", \"descricao\": \"teste\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.status)
    }

    @Test
    @Order(10)
    fun `should create transaction fail when receive invalid body without description`(){
        val requestBody = "{\"valor\": 100000, \"tipo\": \"c\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.status)
    }

    @Test
    @Order(11)
    fun `should create transaction fail when receive invalid body with empty description`(){
        val requestBody = "{\"valor\": 100000, \"tipo\": \"d\", \"descricao\": \"\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.status)
    }

    @Test
    @Order(12)
    fun `should create transaction fail when receive valid body but path with invalid client id`(){
        val requestBody = "{\"valor\": 100000, \"tipo\": \"c\", \"descricao\": \"teste\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/6/transacoes", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))
        }

        assertEquals(HttpStatus.NOT_FOUND, thrown.status)
    }

    @Test
    @Order(13)
    fun `should create transaction fail when receive invalid body with description more than 10 chars`(){
        val requestBody = "{\"valor\": 100000, \"tipo\": \"c\", \"descricao\": \"test_more_than_10_chars\"}"
        val request: MutableHttpRequest<String>? = HttpRequest.POST("/clientes/1/transacoes", requestBody)

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(TransacaoResponse::class.java))
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.status)
    }

    @Test
    @Order(14)
    fun `should get statement success when receive valid id`(){
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/clientes/1/extrato")
        val response = httpClient.toBlocking().exchange(request, Argument.listOf(ExtratoResponse::class.java))

        assertNotNull(response)
        val extrato = response.body.get().first() as ExtratoResponse
        assertNotNull(extrato)
        assertFalse(extrato.ultimasTransacoes.isEmpty())
        assertTrue(extrato.ultimasTransacoes.size == 2)
        assertEquals(extrato.saldoResponse.limite, 100000)
        assertEquals(extrato.saldoResponse.total, 0)
        assertNotNull(extrato.saldoResponse.dataExtrato)
        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    @Order(15)
    fun `should get statement fail when receive invalid id`(){
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/clientes/6/extrato")

        val thrown = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, Argument.listOf(ExtratoResponse::class.java))
        }

        assertEquals(HttpStatus.NOT_FOUND, thrown.status)
    }
}