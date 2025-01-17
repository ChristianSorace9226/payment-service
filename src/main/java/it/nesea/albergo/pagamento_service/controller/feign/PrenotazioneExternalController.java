package it.nesea.albergo.pagamento_service.controller.feign;

import it.nesea.albergo.common_lib.dto.InfoPrenotazione;
import it.nesea.albergo.common_lib.dto.response.CustomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "prenotazione_external", url = "http://localhost:8081/api/front-end-service")
public interface PrenotazioneExternalController {

    @PostMapping("/get-prezzo-camera")
    ResponseEntity<CustomResponse<InfoPrenotazione>> getInfoPrenotazione(@RequestBody Integer idPrenotazione);
}
