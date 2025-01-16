package it.nesea.albergo.pagamento_service.model.repository;

import it.nesea.albergo.pagamento_service.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
}
