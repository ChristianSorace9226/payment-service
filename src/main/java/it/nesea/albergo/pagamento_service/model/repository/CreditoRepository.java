package it.nesea.albergo.pagamento_service.model.repository;

import it.nesea.albergo.pagamento_service.model.Credito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditoRepository extends JpaRepository<Credito, Integer> {
    Credito findByIdUtente(Integer idUtente);
}
