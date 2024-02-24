package io.github.luizimcpi.web.dto.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class TransacaoResponse(
        val limite: Long,
        val saldo: Long
)
