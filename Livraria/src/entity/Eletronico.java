package entity;

import config.MysqlDBConnection;

import java.sql.*;

public class Eletronico extends Livro {

    private final double tamanho;

    public Eletronico(String titulo,
                      String autores,
                      String editora,
                      double preco,
                      double tamanho) {
        super(titulo, autores, editora, preco);
        this.tamanho = tamanho;
    }

    public Eletronico(int id,
                      String titulo,
                      String autores,
                      String editora,
                      double preco,
                      double tamanho) {
        super(id, titulo, autores, editora, preco);
        this.tamanho = tamanho;
    }

    @Override
    protected String getTipo() {
        return "ELETRONICO";
    }

    @Override
    public void saveSpecificDetails() throws SQLException {
        Connection conn = MysqlDBConnection.getConnection();
        String sql = "INSERT INTO eletronico (id, tamanho) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setDouble(2, tamanho);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Eletronico that)) return false;
        if (!super.equals(o)) return false;

        return Double.compare(tamanho, that.tamanho) == 0;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        final int prime = 31;

        hash *= prime + Double.hashCode(tamanho);

        if (hash < 0) hash = -hash;

        return hash;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"super\": " + super.toString() + ",\n" +
                "  \"tamanho\": " + tamanho + "KB\n" +
                "}";
    }
}


