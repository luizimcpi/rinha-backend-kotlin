package io.github.luizimcpi.web.controller

import io.github.luizimcpi.exception.NotFoundException
import io.github.luizimcpi.exception.UnprocessableEnityException
import io.github.luizimcpi.service.TransacaoService
import io.github.luizimcpi.web.dto.request.TransacaoRequest
import io.github.luizimcpi.web.dto.response.ExtratoResponse
import io.github.luizimcpi.web.dto.response.TransacaoResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@Controller("/clientes")
class ClienteController (private val transacaoService: TransacaoService) {


    @Post("/{id}/transacoes")
    fun criaTransacao(@Body @Valid transacaoRequest: TransacaoRequest, id: Long): HttpResponse<TransacaoResponse> {
        verifyRequest(transacaoRequest, id)

        return HttpResponse.ok(transacaoService.criaTransacao(transacaoRequest, id))
    }

    private fun verifyRequest(transacaoRequest: TransacaoRequest, id: Long) {
        if(transacaoRequest.valor % 1 != 0f){
            throw UnprocessableEnityException("Campo valor inválido")
        }
        if(transacaoRequest.tipo.isNullOrBlank()){
            throw UnprocessableEnityException("Campo tipo obrigatório")
        }
        if (transacaoRequest.tipo != "d" && transacaoRequest.tipo != "c") {
            throw UnprocessableEnityException("Tipo não suportado")
        }
        if(transacaoRequest.descricao.isNullOrBlank()) {
            throw UnprocessableEnityException("Campo descrição obrigatório")
        }
        if (transacaoRequest.descricao.length > 10) {
            throw UnprocessableEnityException("Tamanho da descrição não suportado")
        }
        if (id < 1 || id > 5) {
            throw NotFoundException("Cliente não existe")
        }
    }

    @Get("/{id}/extrato")
    fun findAllbyUser(id: Long): HttpResponse<ExtratoResponse> {
        if( id < 1 || id > 5){
            throw NotFoundException("Cliente não existe")
        }

        return HttpResponse.ok(transacaoService.extrato(id))
    }


}