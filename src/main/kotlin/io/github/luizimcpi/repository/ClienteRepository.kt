package io.github.luizimcpi.repository

import io.github.luizimcpi.model.entity.Cliente
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.Optional


@Repository
interface ClienteRepository: JpaRepository<Cliente, Long> {

    @Query("select * from clientes WHERE id = :clienteId FOR UPDATE", nativeQuery = true)
    fun findClienteByIdForUpdate(clienteId: Long): Optional<Cliente>
}