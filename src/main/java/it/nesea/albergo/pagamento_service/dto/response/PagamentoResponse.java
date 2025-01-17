package it.nesea.albergo.pagamento_service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagamentoResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 5493284205096854653L;

    BigDecimal creditoResiduo;
    String messaggio;
}
