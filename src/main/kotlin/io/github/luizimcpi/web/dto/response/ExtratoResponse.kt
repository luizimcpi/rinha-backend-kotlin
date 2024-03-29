package io.github.luizimcpi.web.dto.response

import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
data class ExtratoResponse(
    val saldo: SaldoResponse,
    val ultimasTransacoes: List<TransacaoExtratoResponse>
)

@Introspected
data class SaldoResponse(
        val total: Long,
        val dataExtrato: LocalDateTime = LocalDateTime.now(),
        val limite: Long,
)

@Introspected
data class TransacaoExtratoResponse(
        val valor: Int,
        val tipo: String,
        val descricao: String,
        val realizadaEm: LocalDateTime
)