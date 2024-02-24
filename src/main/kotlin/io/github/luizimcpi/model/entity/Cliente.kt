package io.github.luizimcpi.model.entity

import io.micronaut.core.annotation.Introspected
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "clientes")
@Introspected
data class Cliente(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    val limite: Long,

    @Column
    val saldo: Long,

){
    override fun toString(): String {
        return super.toString()
    }
}
