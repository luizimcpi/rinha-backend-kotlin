package io.github.luizimcpi.model.entity

import io.micronaut.core.annotation.Introspected
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "transacoes")
@Introspected
data class Transacao(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    val valor: Int,

    @Column
    val tipo: String,

    @Column
    val descricao: String,

    @Column(name = "cliente_id")
    val clienteId: Long,

    @Column(name = "realizada_em")
    @CreationTimestamp
    val realizadaEm: LocalDateTime? = null,

    ){
    override fun toString(): String {
        return super.toString()
    }
}
