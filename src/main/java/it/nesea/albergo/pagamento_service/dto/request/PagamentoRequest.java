package it.nesea.albergo.pagamento_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagamentoRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -3135263482062830291L;

    @NotNull(message = "codice prenotazione necessario")
    Integer idPrenotazione;

    @NotNull(message = "va definita una modalità di pagamento")
    Boolean anticipo;

}
