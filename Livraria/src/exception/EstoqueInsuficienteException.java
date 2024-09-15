package exception;

public class EstoqueInsuficienteException extends Exception {
    public EstoqueInsuficienteException(int quantidadeSolicitada, int estoqueDisponivel) {
        super("Estoque insuficiente. Quantidade solicitada: " + quantidadeSolicitada +
              ", estoque disponível: " + estoqueDisponivel);
    }
}