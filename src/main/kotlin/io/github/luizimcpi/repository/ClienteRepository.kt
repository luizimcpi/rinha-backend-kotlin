package io.github.luizimcpi.repository

import io.github.luizimcpi.model.entity.Cliente
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository


@Repository
interface ClienteRepository: JpaRepository<Cliente, Long> {

}