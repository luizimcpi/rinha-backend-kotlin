package io.github.luizimcpi.web.dto.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class TransacaoRequest(
        @field:NotNull
        val valor: Long,
        @field:NotBlank
        val tipo: String,
        @field:NotBlank
        val descricao: String
)
