package io.github.luizimcpi.web.dto.request

import io.micronaut.core.annotation.Introspected
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class CustomerRequest(
        @field:Email
        @field:NotBlank
        val email: String,
        @field:NotBlank
        val name: String,
        @field:NotBlank
        val document: String,
        @field:NotNull
        val birthDate: LocalDate
)
