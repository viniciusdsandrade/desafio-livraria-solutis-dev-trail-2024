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
            System.out.println("Estoque insuficiente para a quantidade solicitada.");
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
        this.estoque = estoque;
    }
    public void setFrete(double frete) {
        this.frete = frete;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d, Título: %s, Autores: %s, Editora: %s, Preço: %.2f, Frete: %.2f, Estoque: %d",
                id, titulo, autores, editora, preco, frete, estoque
        );
    }
}
