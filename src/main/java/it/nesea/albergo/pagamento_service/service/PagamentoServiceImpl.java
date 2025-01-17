package it.nesea.albergo.pagamento_service.service;

import it.nesea.albergo.common_lib.dto.InfoPrenotazione;
import it.nesea.albergo.common_lib.exception.NotFoundException;
import it.nesea.albergo.pagamento_service.controller.feign.PrenotazioneExternalController;
import it.nesea.albergo.pagamento_service.dto.request.PagamentoRequest;
import it.nesea.albergo.pagamento_service.dto.response.PagamentoResponse;
import it.nesea.albergo.pagamento_service.model.Credito;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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

    public PagamentoResponse effettuaPagamento(PagamentoRequest request) {
        log.info("Effettuo il pagamento con la prenotazione {}", request.getIdPrenotazione());

        InfoPrenotazione infoPrenotazione = prenotazioneExternalController.getInfoPrenotazione(request.getIdPrenotazione());
        if (infoPrenotazione == null) {
            log.error("Prenotazione non trovata con id {}", request.getIdPrenotazione());
            throw new NotFoundException("Prenotazione non trovata");
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Credito> creditoQuery = cb.createQuery(Credito.class);
        Root<Credito> creditoRoot = creditoQuery.from(Credito.class);
        creditoQuery.where(cb.equal(creditoRoot.get("idUtente"), infoPrenotazione.getIdUtente()));
        Credito credito = entityManager.createQuery(creditoQuery).getSingleResult();
        if(credito != null && credito.getCreditoResiduo().compareTo(infoPrenotazione.getPrezzoTotale()) > 0){
            log.info("Pagamento effettuato con successo per la prenotazione {}", request.getIdPrenotazione());
            credito.setCreditoResiduo(credito.getCreditoResiduo().subtract(infoPrenotazione.getPrezzoTotale()));
            entityManager.merge(credito);
            return new PagamentoResponse();
        }
        return null;
    }
}
