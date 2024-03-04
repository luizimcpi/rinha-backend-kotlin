package io.github.luizimcpi.service

import io.github.luizimcpi.exception.NotFoundException
import io.github.luizimcpi.exception.UnprocessableEnityException
import io.github.luizimcpi.model.entity.Transacao
import io.github.luizimcpi.repository.ClienteRepository
import io.github.luizimcpi.repository.TransacaoRepository
import io.github.luizimcpi.web.dto.request.TransacaoRequest
import io.github.luizimcpi.web.dto.response.ExtratoResponse
import io.github.luizimcpi.web.dto.response.SaldoResponse
import io.github.luizimcpi.web.dto.response.TransacaoExtratoResponse
import io.github.luizimcpi.web.dto.response.TransacaoResponse
import jakarta.inject.Singleton

@Singleton
class TransacaoService(private val clienteRepository: ClienteRepository,
                       private val transacaoRepository: TransacaoRepository
) {

    fun criaTransacao(request: TransacaoRequest, clienteId: Long): TransacaoResponse {
        val cliente = clienteRepository.findClienteByIdForUpdate(clienteId)
        if(cliente.isPresent){
            var saldoAtual = cliente.get().saldo
            if(request.tipo == "d"){
                if (request.valor > saldoAtual + cliente.get().limite) {
                    throw UnprocessableEnityException("saldo inconsistente")
                }
                saldoAtual -= request.valor.toInt()
            }
            if(request.tipo == "c"){
                saldoAtual += request.valor.toInt()
            }

            val clienteAtualizar = cliente.get().copy(saldo = saldoAtual)
            val clienteAtualizado = clienteRepository.update(clienteAtualizar)

            val transacao = Transacao(
                valor = request.valor.toInt(),
                tipo = request.tipo!!,
                descricao = request.descricao!!,
                clienteId = cliente.get().id!!
            )
            transacaoRepository.save(transacao)

            return TransacaoResponse(clienteAtualizado.limite, clienteAtualizado.saldo)
        }
        throw NotFoundException("Cliente não encontrado")
    }

    fun extrato(clienteId: Long): ExtratoResponse {
        val cliente = clienteRepository.findClienteByIdForUpdate(clienteId)
        if(cliente.isPresent) {
            val saldoResponse = SaldoResponse(total = cliente.get().saldo, limite = cliente.get().limite)
            var transacoes = transacaoRepository.findTop10ByClienteIdOrderByRealizadaEmDesc(clienteId).map {
                TransacaoExtratoResponse(it.valor, it.tipo, it.descricao, it.realizadaEm!!)
            }

            return ExtratoResponse(saldo = saldoResponse, ultimasTransacoes = transacoes)
        }
        throw NotFoundException("Cliente não encontrado")
    }
}