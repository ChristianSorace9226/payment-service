package it.nesea.albergo.pagamento_service.exception;

public class ExpiredException extends RuntimeException {
    public ExpiredException(String message) {
        super(message);
    }
}
