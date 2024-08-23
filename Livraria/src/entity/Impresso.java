package entity;

import config.MysqlDBConnection;

import java.sql.*;

public class Impresso extends Livro {

    private final double frete;
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
            Connection conn = MysqlDBConnection.getConnection();
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
        Connection conn = MysqlDBConnection.getConnection();
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
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Impresso impresso)) return false;
        if (!super.equals(o)) return false;

        return Double.compare(frete, impresso.frete) == 0 &&
                estoque == impresso.estoque;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        final int prime = 31;

        hash *= prime + Double.hashCode(frete);
        hash *= prime + estoque;

        if (hash < 0) hash = -hash;

        return hash;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"frete\": \"" + frete + "\",\n" +
                "  \"estoque\": \"" + estoque + "\"\n" +
                "}";
    }
}
