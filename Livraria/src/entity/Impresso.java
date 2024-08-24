package entity;

import java.sql.*;

import static config.MySQLConnection.getConnection;
import static java.lang.Double.compare;

/*
    3.2 Impresso
    A classe Impresso representa um livro impresso e possui 2 atributos:
        a) frete: frete cobrado para entrega do livro;
        b) estoque: número de exemplares do livro em estoque.

    A seguir são descritos os métodos da classe Impresso:
        a) void atualizarEstoque(): este metodo deve subtrair 1 do valor do atributo estoque;
        b) String toString: este metodo devolve uma representação textual de todos dos atributos de um livro impresso.
 */
public class Impresso extends Livro {

    private double frete;
    private int estoque;

    public Impresso(String titulo,
                    String autores,
                    String editora,
                    double preco,
                    double frete,
                    int estoque) {
        super(titulo, autores, editora, preco);
        this.frete = frete;
        this.estoque = estoque;
    }

    public Impresso(int id,
                    String titulo,
                    String autores,
                    String editora,
                    double preco,
                    double frete,
                    int estoque) {
        super(id, titulo, autores, editora, preco);
        this.frete = frete;
        this.estoque = estoque;
    }

    public void atualizarEstoque(int quantidade) throws SQLException { // Recebe a quantidade como parâmetro
        if (estoque >= quantidade) { // Verifica se há estoque suficiente
            estoque -= quantidade; // Subtrai a quantidade do estoque

            Connection conn = getConnection();
            String sql = "UPDATE impresso SET estoque = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, estoque);
            stmt.setInt(2, id);
            stmt.executeUpdate();

            stmt.close();
            conn.close();
        } else {
            // Lidar com a situação em que não há estoque suficiente (opcional)
            System.err.println("Estoque insuficiente para a quantidade solicitada.");
        }
    }

    @Override
    protected String getTipo() {
        return "IMPRESSO";
    }

    @Override
    public void saveSpecificDetails() throws SQLException {
        Connection conn = getConnection();
        String sql = "INSERT INTO impresso (id, frete, estoque) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, id);
        stmt.setDouble(2, frete);
        stmt.setInt(3, estoque);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public int getEstoque() {
        return estoque;
    }
    public double getFrete() {
        return frete;
    }

    public void setEstoque(int estoque) {
        if (estoque >= 0) this.estoque = estoque;
        else throw new IllegalArgumentException("Estoque não pode ser negativo.");
    }
    public void setFrete(double frete) {
        if (frete >= 0) this.frete = frete;
        else throw new IllegalArgumentException("Frete não pode ser negativo.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Impresso that = (Impresso) o;

        return compare(this.frete, that.frete) == 0 &&
                this.estoque == that.estoque;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = super.hashCode();

        hash *= prime + Double.hashCode(frete);
        hash *= prime + estoque;

        if (hash < 0) hash = -hash;

        return hash;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d, Título: %s, Autores: %s, Editora: %s, Preço: %.2f, Frete: %.2f, Estoque: %d",
                id, titulo, autores, editora, preco, frete, estoque
        );
    }
}
