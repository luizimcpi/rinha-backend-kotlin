package io.github.luizimcpi.repository

import io.github.luizimcpi.model.entity.Transacao
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.transaction.annotation.ReadOnly


@Repository
interface TransacaoRepository: JpaRepository<Transacao, Long> {
    @ReadOnly
    fun findTop10ByClienteIdOrderByRealizadaEmDesc(clienteId: Long): List<Transacao>
}