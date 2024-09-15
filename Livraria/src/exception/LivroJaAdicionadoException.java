package exception;

import entity.Livro;

public class LivroJaAdicionadoException extends Exception {
    public LivroJaAdicionadoException(Livro livro) {
        super("O livro '" + livro.getTitulo() + "' já foi adicionado a esta venda.");
    }
}