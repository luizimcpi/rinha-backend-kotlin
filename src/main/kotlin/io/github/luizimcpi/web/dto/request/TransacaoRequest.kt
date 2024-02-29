package io.github.luizimcpi.web.dto.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Min

@Introspected
data class TransacaoRequest(
        @field:Min(1L)
        val valor: Float,
        val tipo: String? = "",
        val descricao: String? = ""
)
