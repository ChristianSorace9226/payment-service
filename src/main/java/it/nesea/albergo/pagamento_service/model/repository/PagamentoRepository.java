package it.nesea.albergo.pagamento_service.model.repository;

import it.nesea.albergo.pagamento_service.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
    Optional<Pagamento> findByIdPrenotazione(Integer idPrenotazione);
}
