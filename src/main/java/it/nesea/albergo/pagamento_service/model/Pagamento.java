package it.nesea.albergo.pagamento_service.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "PAGAMENTO", schema = "PAGAMENTO_SERVICE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pagamento implements Serializable {
    @Serial
    private static final long serialVersionUID = 5285270280247710189L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagamentoGenerator")
    @SequenceGenerator(name = "pagamentoGenerator", schema = "pagamento_service", sequenceName = "seq_pagamento", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 4)
    Integer id;

    @Column(name = "ID_PRENOTAZIONE")
    Integer idPrenotazione;

    @Column(name = "ANNULLATO")
    Boolean annullato;


}
