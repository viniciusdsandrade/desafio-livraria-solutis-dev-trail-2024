package entity;

import config.DBConnection;

import java.sql.*;

public class Eletronico extends Livro {
    private double tamanho;

    public Eletronico(String titulo, String autores, String editora, double preco, double tamanho) {
        super(titulo, autores, editora, preco);
        this.tamanho = tamanho;
    }

    public Eletronico(int id, String titulo, String autores, String editora, double preco, double tamanho) {
        super(id, titulo, autores, editora, preco);
        this.tamanho = tamanho;
    }

    @Override
    protected String getTipo() {
        return "ELETRONICO";
    }

    @Override
    public void saveSpecificDetails() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO eletronico (id, tamanho) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setDouble(2, tamanho);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    @Override
    public String toString() {
        return super.toString() + ", Tamanho: " + tamanho + "KB";
    }
}


