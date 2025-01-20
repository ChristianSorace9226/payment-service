package it.nesea.albergo.pagamento_service.util;

import it.nesea.albergo.common_lib.dto.InfoPrenotazione;
import it.nesea.albergo.pagamento_service.dto.request.PagamentoRequest;
import it.nesea.albergo.pagamento_service.model.Pagamento;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class Util {

    public void gestisciPagamentoAnticipo(Pagamento pagamento, InfoPrenotazione prenotazione) {
        BigDecimal anticipo = prenotazione.getPrezzoTotale().multiply(BigDecimal.valueOf(0.5));
        pagamento.setIdStatoPagamento(2);
        pagamento.setImportoAnticipo(anticipo);
        pagamento.setImportoSaldo(anticipo);
        pagamento.setDataAnticipo(LocalDate.now());
        pagamento.setScadenzaSaldo(calcolaScadenzaSaldo(pagamento, prenotazione.getCheckIn()));
    }

    public LocalDate calcolaScadenzaSaldo(Pagamento pagamento, LocalDate checkIn) {
        // Calcola la data limite di 3 settimane dall'anticipo
        LocalDate scadenzaTreSettimane = pagamento.getDataAnticipo().plusWeeks(3);

        // Calcola la data limite di 15 giorni prima del check-in
        LocalDate scadenzaQuindiciGiorni = checkIn.minusDays(15);

        // Restituisci la data più vicina tra le due
        if (scadenzaTreSettimane.isBefore(scadenzaQuindiciGiorni)) {
            return scadenzaTreSettimane;
        } else {
            return scadenzaQuindiciGiorni;
        }
    }
    public Pagamento creaPagamento(PagamentoRequest request, InfoPrenotazione prenotazione) {
        Pagamento pagamento = new Pagamento();
        if (request.getAnticipo()) {
            gestisciPagamentoAnticipo(pagamento, prenotazione);
        } else {
            pagamento.setIdStatoPagamento(1);
            pagamento.setImportoSaldo(prenotazione.getPrezzoTotale());
        }
        pagamento.setIdPrenotazione(request.getIdPrenotazione());
        pagamento.setIdMetodoPagamento(prenotazione.getIdMetodoPagamento());
        return pagamento;
    }

    public void gestisciStatoPagamento(Pagamento pagamento, InfoPrenotazione prenotazione) {
        switch (pagamento.getIdStatoPagamento()) {
            case 1: // Saldo pagato
                throw new IllegalStateException("Pagamento già effettuato");
            case 2: // Anticipo pagato
                completaPagamentoAnticipo(pagamento, prenotazione);
                break;
            case 3: // Pagamento annullato
                throw new IllegalStateException("Pagamento annullato");
            default:
                throw new IllegalStateException("Stato pagamento non valido");
        }
    }

    public void completaPagamentoAnticipo(Pagamento pagamento, InfoPrenotazione prenotazione) {
        BigDecimal importoAnticipo = pagamento.getImportoAnticipo();
        BigDecimal importoPagato = importoAnticipo.multiply(BigDecimal.TWO);

        if (importoPagato.compareTo(prenotazione.getPrezzoTotale()) >= 0) {
            pagamento.setIdStatoPagamento(1);
            pagamento.setImportoAnticipo(null);
            pagamento.setImportoSaldo(importoPagato);
        }
    }
}
