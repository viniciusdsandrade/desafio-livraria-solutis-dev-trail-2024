package entity;

import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class Venda {
    private int id;
    private String cliente;
    private double valor;
    private ArrayList<Livro> livros;

    public Venda(String cliente) {
        this.cliente = cliente;
        this.livros = new ArrayList<>();
    }

    public void addLivro(Livro l) {
        livros.add(l);
        valor += l.getPreco();
    }

    public void save() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO venda (cliente, valor) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, cliente);
        stmt.setDouble(2, valor);
        stmt.executeUpdate();

        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            id = generatedKeys.getInt(1);
        }

        stmt.close();

        for (Livro livro : livros) {
            String sqlVendaLivro = "INSERT INTO venda_livro (venda_id, livro_id) VALUES (?, ?)";
            PreparedStatement stmtVendaLivro = conn.prepareStatement(sqlVendaLivro);
            stmtVendaLivro.setInt(1, id);
            stmtVendaLivro.setInt(2, livro.getId());
            stmtVendaLivro.executeUpdate();
            stmtVendaLivro.close();
        }

        conn.close();
    }

    @Override
    public String toString() {
        return "Venda Nº " + id + ", Cliente: " + cliente + ", Valor Total: " + valor;
    }
}


