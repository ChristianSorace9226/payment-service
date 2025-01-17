package it.nesea.albergo.pagamento_service.exception;

public class CreditoNonSufficienteException extends RuntimeException {
    public CreditoNonSufficienteException(String message) {
        super(message);
    }
}
