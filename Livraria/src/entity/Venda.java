package entity;

public class Venda {
    private static int numVendas = 0;
    private final Livro[] livros;
    private final int numero;
    private final String cliente;
    private double valor;

    public Venda(int quantidadeLivros, String cliente) {
        this.livros = new Livro[quantidadeLivros];
        this.numero = ++numVendas;
        this.cliente = cliente;
        this.valor = 0;
    }

    public void addLivro(Livro l, int index) {
        if (index >= 0 && index < livros.length) {
            livros[index] = l;
            valor += l.getPreco();
        }
    }

    public void listarLivros() {
        for (Livro livro : livros) {
            if (livro != null) {
                System.out.println(livro.toString());
            }
        }
    }

    @Override
    public String toString() {
        return "Venda Nº " + numero + ", Cliente: " + cliente + ", Valor Total: " + valor;
    }
}

