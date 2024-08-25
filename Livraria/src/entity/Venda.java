package entity;

import java.sql.*;
import java.util.Arrays;

import static config.MySQLConnection.getConnection;
import static java.lang.Double.compare;

/*
    3.4 Venda
    A classe Venda possui 5 atributos:
        a) livros: um vetor de referências a objetos do tipo Livro. Representa os livros associados a uma venda;
        b) numVendas: atributo estático que representa a quantidade de vendas realizadas. Deve ser incrementado de 1 sempre que uma nova venda for realizada;
        c) numero: representa o número da venda. É um valor sequencial com início em 1 e é incrementado a cada venda.
        Utilize o valor do atributo numVendas para definir o valor desse atributo;
        d) cliente: nome do cliente que comprou o(s) livro(s);
        e) valor: valor total da venda.

        A seguir são descritos os métodos da classe Venda:
        a) addLivro(l: Livro, index: int): adiciona o livro l na posição index do array livros;
        b) listarLivros(): lista todos os livros da venda.
 */
public class Venda {
    private static int numVendas = 0;

    private int id;
    private int numero;
    private String cliente;
    private double valor;
    private final Livro[] livros;

    public Venda(String cliente) {
        this.cliente = cliente;
        this.livros = new Livro[50]; // Numero de livros por venda
        this.numero = ++numVendas;
        this.valor = 0;
    }

    public void addLivro(Livro l, int quantidade) {
        for (int i = 0; i < livros.length; i++) {
            if (livros[i] == null) {
                livros[i] = l;
                valor += l.getPreco() * quantidade; // Multiplique o preço pela quantidade
                return;
            }
        }
        // Se o vetor estiver cheio, você pode aumentar o tamanho ou lançar uma exceção
        System.err.println("Vetor de livros cheio!");
    }

    public void listarLivros() {
        for (Livro livro : livros) {
            if (livro != null) {
                System.out.println(livro);
            }
        }
    }

    public void save() throws SQLException {
        Connection conn = getConnection();
        String sql = "INSERT INTO venda (cliente, valor) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, cliente);
        stmt.setDouble(2, valor);
        stmt.executeUpdate();

        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) id = generatedKeys.getInt(1);

        stmt.close();

        for (Livro livro : livros) {
            if (livro != null) {
                String sqlVendaLivro = "INSERT INTO venda_livro (venda_id, livro_id) VALUES (?, ?)";
                PreparedStatement stmtVendaLivro = conn.prepareStatement(sqlVendaLivro);

                stmtVendaLivro.setInt(1, id);
                stmtVendaLivro.setInt(2, livro.getId());

                stmtVendaLivro.executeUpdate();
                stmtVendaLivro.close();
            }
        }
        conn.close();
    }

    public static int getNumVendas() {
        return numVendas;
    }
    public int getId() {
        return id;
    }
    public double getValor() {
        return valor;
    }
    public String getCliente() {
        return cliente;
    }
    public int getNumero() {
        return numero;
    }

    public static void setNumVendas(int numVendas) {
        Venda.numVendas = numVendas;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;

        Venda that = (Venda) o;

        return this.id == that.id &&
                this.numero == that.numero &&
                compare(this.valor, that.valor) == 0 &&
                this.cliente.equals(that.cliente) &&
                Arrays.equals(this.livros, that.livros);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = id;

        result *= prime + numero;
        result *= prime + cliente.hashCode();
        result *= prime + Double.hashCode(valor);
        result *= prime + Arrays.hashCode(livros);

        if (result < 0) result *= -1;

        return result;
    }

    // todo: Implementar o método toString com a correção das vendas
    @Override
    public String toString() {
        StringBuilder json = new StringBuilder();

        json.append("{")
                .append("\"id\": ").append(id).append(", ")
                .append("\"cliente\": \"").append(cliente).append("\", ")
                .append("\"valorTotal\": ").append(valor).append(", ") // Nome mais descritivo
                .append("\"livros\": [");

        for (Livro livro : livros) {
            if (livro != null) {
                json.append("{");
                json.append("\"nome\": \"").append(livro.getTitulo()).append("\", ");
                json.append("\"valorUnitario\": ").append(livro.getPreco());

            }
        }

        if (json.toString().endsWith(", ")) json.delete(json.length() - 2, json.length());

        json.append("]").append("}");

        return json.toString();
    }
}
