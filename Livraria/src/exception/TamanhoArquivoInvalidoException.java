package exception;

public class TamanhoArquivoInvalidoException extends IllegalArgumentException {
    public TamanhoArquivoInvalidoException(long tamanho) {
        super("Tamanho do arquivo inválido: " + tamanho + ". O tamanho deve ser maior que zero.");
    }
}
