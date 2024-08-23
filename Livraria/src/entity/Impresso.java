package entity;

import java.sql.*;

import static config.MySQLConnection.getConnection;

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

    public void atualizarEstoque() throws SQLException {
        if (estoque > 0) {
            estoque--;
            Connection conn = getConnection();
            String sql = "UPDATE impresso SET estoque = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, estoque);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
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

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": \"" + id + "\",\n" +
                "  \"titulo\": \"" + titulo + "\",\n" +
                "  \"autores\": \"" + autores + "\",\n" +
                "  \"editora\": \"" + editora + "\",\n" +
                "  \"preco\": \"" + preco + "\",\n" +
                "  \"frete\": \"" + frete + "\",\n" +
                "  \"estoque\": \"" + estoque + "\"\n" +
                "}";
    }
}
