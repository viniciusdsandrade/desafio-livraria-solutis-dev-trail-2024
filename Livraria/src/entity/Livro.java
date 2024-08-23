package entity;

import config.MysqlDBConnection;

import java.sql.*;

public abstract class Livro {

    protected int id;
    protected String titulo;
    protected String autores;
    protected String editora;
    protected double preco;

    public Livro(String titulo,
                 String autores,
                 String editora,
                 double preco) {
        this.titulo = titulo;
        this.autores = autores;
        this.editora = editora;
        this.preco = preco;
    }

    public Livro(int id,
                 String titulo,
                 String autores,
                 String editora,
                 double preco) {
        this.id = id;
        this.titulo = titulo;
        this.autores = autores;
        this.editora = editora;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public double getPreco() {
        return preco;
    }

    public String getEditora() {
        return editora;
    }

    public String getAutores() {
        return autores;
    }

    public String getTitulo() {
        return titulo;
    }

    public void save() throws SQLException {
        Connection conn = MysqlDBConnection.getConnection();
        String sql = "INSERT INTO livro (titulo, autores, editora, preco, tipo) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, titulo);
        stmt.setString(2, autores);
        stmt.setString(3, editora);
        stmt.setDouble(4, preco);
        stmt.setString(5, getTipo());
        stmt.executeUpdate();

        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            id = generatedKeys.getInt(1);
        }

        stmt.close();
        conn.close();
    }

    protected abstract String getTipo();

    public abstract void saveSpecificDetails() throws SQLException;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Livro that)) return false;

        return this.id == that.id &&
                Double.compare(this.preco, that.preco) == 0 &&
                this.titulo.equals(that.titulo) &&
                this.autores.equals(that.autores) &&
                this.editora.equals(that.editora);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = id;

        hash *= prime + titulo.hashCode();
        hash *= prime + autores.hashCode();
        hash *= prime + editora.hashCode();
        hash *= prime + Double.hashCode(preco);

        if (hash < 0) hash = -hash;

        return hash;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"Título\": \"" + titulo + "\",\n" +
                "  \"Autores\": \"" + autores + "\",\n" +
                "  \"Editora\": \"" + editora + "\",\n" +
                "  \"Preço\": " + preco + "\n" +
                "}";
    }
}
