package io.github.luizimcpi.web.dto.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class TransacaoRequest(
        @field:Min(1)
        val valor: Long,
        @field:NotNull
        @field:NotBlank
        val tipo: String,
        @field:NotNull
        @field:NotBlank
        val descricao: String
)
