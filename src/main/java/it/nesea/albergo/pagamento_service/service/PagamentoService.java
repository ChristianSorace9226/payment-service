package it.nesea.albergo.pagamento_service.service;

import it.nesea.albergo.pagamento_service.dto.request.PagamentoRequest;
import it.nesea.albergo.pagamento_service.dto.response.PagamentoResponse;

public interface PagamentoService {
    PagamentoResponse effettuaPagamento(PagamentoRequest request);
}
