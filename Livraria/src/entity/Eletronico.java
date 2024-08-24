package entity;

import java.sql.*;

import static config.MySQLConnection.getConnection;

/*
    3.3 Eletronico
    A classe Eletronico representa um livro eletrônico e possui 1 atributo:
        a) tamanho: representa o tamanho do arquivo eletrônico do livro em KB.

    A seguir é descrito mais um metodo obrigatório da classe Eletronico:
        a) String toString: este metodo devolve uma representação textual de todos dos atributos de um livro eletrônico.
 */
public class Eletronico extends Livro {

    private long tamanho;

    public Eletronico(String titulo,
                      String autores,
                      String editora,
                      double preco,
                      long tamanho) {
        super(titulo, autores, editora, preco);
        this.tamanho = tamanho;
    }

    public Eletronico(int id,
                      String titulo,
                      String autores,
                      String editora,
                      double preco,
                      long tamanho) {
        super(id, titulo, autores, editora, preco);
        this.tamanho = tamanho;
    }

    @Override
    protected String getTipo() {
        return "ELETRONICO";
    }

    @Override
    public void saveSpecificDetails() throws SQLException {
        Connection conn = getConnection();
        String sql = "INSERT INTO eletronico (id, tamanho) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, id);
        stmt.setDouble(2, tamanho);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public double getTamanho() {
        return tamanho;
    }
    public void setTamanho(long tamanho) {
        if (tamanho <= 0) throw new IllegalArgumentException("O tamanho do arquivo deve ser maior que zero.");
        this.tamanho = tamanho;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Chama o equals da classe pai

        Eletronico that = (Eletronico) o;

        return this.tamanho == that.tamanho;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = super.hashCode(); // Inicia com o hashCode da classe pai

        hash *= prime + Long.hashCode(tamanho);

        if (hash < 0) hash = -hash;

        return hash;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d, Título: %s, Autores: %s, Editora: %s, Preço: %.2f, Tamanho: %d KB",
                id, titulo, autores, editora, preco, tamanho
        );
    }
}
