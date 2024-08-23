package entity;

import config.MysqlDBConnection;

import java.sql.*;
import java.util.ArrayList;

import static java.util.Objects.hash;

public class Venda {

    private int id;
    private final String cliente;
    private double valor;
    private final ArrayList<Livro> livros;

    public Venda(String cliente) {
        this.cliente = cliente;
        this.livros = new ArrayList<>();
    }

    public void addLivro(Livro l) {
        livros.add(l);
        valor += l.getPreco();
    }

    public void save() throws SQLException {
        Connection conn = MysqlDBConnection.getConnection();
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

    public String getCliente() {
        return cliente;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;

        Venda that = (Venda) o;

        if (id != that.id) return false;
        if (Double.compare(that.valor, valor) != 0) return false;
        if (!cliente.equals(that.cliente)) return false;

        for (int i = 0; i < livros.size(); i++) {
            Livro livro1 = livros.get(i);
            Livro livro2 = that.livros.get(i);
            if (!livro1.equals(livro2)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = hash(id, cliente, valor);

        // Calculando o hashCode para cada livro da lista
        hash *= livros.stream().mapToInt(livro -> prime + (livro != null ? livro.hashCode() : 0)).reduce(1, (a, b) -> a * b);

        if (hash < 0) hash = -hash;

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder json = new StringBuilder();

        json.append("{")
                .append("\"id\": ").append(id).append(", ")
                .append("\"cliente\": \"").append(cliente).append("\", ")
                .append("\"valor\": ").append(valor).append(", ")
                .append("\"livros\": [");

        for (int i = 0; i < livros.size(); i++) {
            json.append(livros.get(i).toString()); // Presume que Livro tenha um toString que segue o padrão JSON
            if (i < livros.size() - 1) {
                json.append(", ");
            }
        }

        json.append("]")
                .append("}");

        return json.toString();
    }
}


