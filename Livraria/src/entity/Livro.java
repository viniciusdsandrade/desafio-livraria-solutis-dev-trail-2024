package entity;

import config.DBConnection;

import java.sql.*;

public abstract class Livro {
    protected int id;
    protected String titulo;
    protected String autores;
    protected String editora;
    protected double preco;

    public Livro(String titulo, String autores, String editora, double preco) {
        this.titulo = titulo;
        this.autores = autores;
        this.editora = editora;
        this.preco = preco;
    }

    public Livro(int id, String titulo, String autores, String editora, double preco) {
        this.id = id;
        this.titulo = titulo;
        this.autores = autores;
        this.editora = editora;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public void save() throws SQLException {
        Connection conn = DBConnection.getConnection();
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
    public String toString() {
        return "Título: " + titulo + ", Autores: " + autores + ", Editora: " + editora + ", Preço: " + preco;
    }

    public double getPreco() {
        return preco;
    }
}
