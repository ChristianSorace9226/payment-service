package it.nesea.albergo.pagamento_service.service;

import it.nesea.albergo.common_lib.dto.InfoPrenotazione;
import it.nesea.albergo.common_lib.exception.NotFoundException;
import it.nesea.albergo.pagamento_service.controller.feign.PrenotazioneExternalController;
import it.nesea.albergo.pagamento_service.dto.request.PagamentoRequest;
import it.nesea.albergo.pagamento_service.dto.response.PagamentoResponse;
import it.nesea.albergo.pagamento_service.exception.CreditoNonSufficienteException;
import it.nesea.albergo.pagamento_service.model.Credito;
import it.nesea.albergo.pagamento_service.model.repository.CreditoRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
@Slf4j
public class PagamentoServiceImpl implements PagamentoService {

    private final EntityManager entityManager;
    private final PrenotazioneExternalController prenotazioneExternalController;
    private final CreditoRepository creditoRepository;

    public PagamentoResponse effettuaPagamento(PagamentoRequest request) {
        log.info("Effettuo il pagamento con la prenotazione {}", request.getIdPrenotazione());

        InfoPrenotazione infoPrenotazione = prenotazioneExternalController.getInfoPrenotazione(request.getIdPrenotazione()).getBody().getResponse();
        if (infoPrenotazione == null) {
            log.error("Prenotazione non trovata con id {}", request.getIdPrenotazione());
            throw new NotFoundException("Prenotazione non trovata");
        }

        Credito credito = creditoRepository.findByIdUtente(infoPrenotazione.getIdUtente());

        if (credito != null && credito.getCreditoResiduo().compareTo(infoPrenotazione.getPrezzoTotale()) >= 0) {
            log.info("Pagamento effettuato con successo per la prenotazione {}", request.getIdPrenotazione());
            credito.setCreditoResiduo(credito.getCreditoResiduo().subtract(infoPrenotazione.getPrezzoTotale()));
            creditoRepository.save(credito);
            return new PagamentoResponse(credito.getCreditoResiduo(), "Pagamento andato a buon fine");
        }
        throw new CreditoNonSufficienteException("Credito esaurito. Ricaricare per continuare");
    }
}
