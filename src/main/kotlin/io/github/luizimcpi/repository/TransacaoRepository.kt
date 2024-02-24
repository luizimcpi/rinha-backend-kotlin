package io.github.luizimcpi.repository

import io.github.luizimcpi.model.entity.Transacao
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository


@Repository
interface TransacaoRepository: JpaRepository<Transacao, Long> {
    fun findTop10ByClienteIdOrderByRealizadaEmDesc(clienteId: Long): List<Transacao>
}