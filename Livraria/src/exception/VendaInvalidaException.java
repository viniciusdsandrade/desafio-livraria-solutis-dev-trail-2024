package exception;

public class VendaInvalidaException extends Exception {
    public VendaInvalidaException(String mensagem) {
        super(mensagem);
    }

    public VendaInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}