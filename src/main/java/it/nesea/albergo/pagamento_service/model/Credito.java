package it.nesea.albergo.pagamento_service.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "CREDITO", schema = "PAGAMENTO_SERVICE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "creditoGenerator")
    @SequenceGenerator(name = "creditoGenerator", schema = "pagamento_service", sequenceName = "seq_credito", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 4)
    Integer id;

    @Column(name = "CREDITO_RESIDUO")
    BigDecimal creditoResiduo;

    @Column(name = "ID_UTENTE")
    Integer idUtente;
}
