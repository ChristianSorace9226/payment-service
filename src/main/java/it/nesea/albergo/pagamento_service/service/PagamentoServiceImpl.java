package it.nesea.albergo.pagamento_service.service;

import it.nesea.albergo.common_lib.dto.InfoPrenotazione;
import it.nesea.albergo.pagamento_service.controller.feign.PrenotazioneExternalController;
import it.nesea.albergo.pagamento_service.dto.request.PagamentoRequest;
import it.nesea.albergo.pagamento_service.dto.response.PagamentoResponse;
import it.nesea.albergo.pagamento_service.exception.ExpiredException;
import it.nesea.albergo.pagamento_service.model.Pagamento;
import it.nesea.albergo.pagamento_service.model.repository.PagamentoRepository;
import it.nesea.albergo.pagamento_service.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Data
@AllArgsConstructor
@Slf4j
public class PagamentoServiceImpl implements PagamentoService {

    private final PrenotazioneExternalController prenotazioneExternalController;
    private final PagamentoRepository pagamentoRepository;
    private final Util util;

    public PagamentoResponse effettuaPagamento(PagamentoRequest request) {
        InfoPrenotazione prenotazione = prenotazioneExternalController
                .getInfoPrenotazione(request.getIdPrenotazione()).getBody().getResponse();

        Optional<Pagamento> pagamentoFind = pagamentoRepository.findByIdPrenotazione(request.getIdPrenotazione());

        if (pagamentoFind.isPresent()) {
            Pagamento pagamentoAnticipo = pagamentoFind.get();
            log.info("Gestione pagamento esistente per prenotazione [{}]", prenotazione);

            if (pagamentoAnticipo.getScadenzaSaldo().isBefore(LocalDate.now()) ||
                    prenotazione.getCheckIn().minusDays(15).isBefore(LocalDate.now())) {
                pagamentoAnticipo.setIdStatoPagamento(3);
                pagamentoRepository.save(pagamentoAnticipo);
                throw new ExpiredException("Pagamento annullato per scadenza");
            }

            util.gestisciStatoPagamento(pagamentoAnticipo, prenotazione);
            pagamentoRepository.save(pagamentoAnticipo);
        } else {
            Pagamento nuovoPagamento = util.creaPagamento(request, prenotazione);
            pagamentoRepository.save(nuovoPagamento);
        }

        return new PagamentoResponse("Pagamento andato a buon fine");
    }
}
