package entity;

import config.MySQLConnection;

import java.sql.*;

/*
    3.1 Livro
    A classe abstrata Livro possui 4 atributos:
    a) titulo: título do livro;
    b) autores: nome do autor ou dos autores do livro;
    c) editora: nome da editora do livro;
    d) preco: preço do livro.

    Os métodos de acesso (getters e setters) e o(s) construtor(es) desta
    classe e das demais classes foram omitidos e devem ser implementados
    mesmo que você não os julgue necessário.

    O outro metodo obrigatório da classe Livro é descrito a seguir:
    a) String toString(): devolve uma representação textual dos atributos de umlivro.
 */

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

    public void save() throws SQLException {
        Connection conn = MySQLConnection.getConnection();
        String sql = "INSERT INTO livro (titulo, autores, editora, preco, tipo) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, titulo);
        stmt.setString(2, autores);
        stmt.setString(3, editora);
        stmt.setDouble(4, preco);
        stmt.setString(5, getTipo());
        stmt.executeUpdate();

        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) id = generatedKeys.getInt(1);

        stmt.close();
        conn.close();
    }

    protected abstract String getTipo();
    public abstract void saveSpecificDetails() throws SQLException;

    public String getAutores() {
        return autores;
    }
    public int getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getEditora() {
        return editora;
    }
    public double getPreco() {
        return preco;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setEditora(String editora) {
        this.editora = editora;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d, Título: %s, Autores: %s, Editora: %s, Preço: %.2f",
                id, titulo, autores, editora, preco
        );
    }
}
