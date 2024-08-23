package entity;

import java.sql.*;

import static config.MySQLConnection.getConnection;
import static input.EntradaDeDados.*;

public class LivrariaVirtual {

    public void cadastrarLivro() {
        String titulo, autores, editora;
        double preco;
        int tipoLivro;

        tipoLivro = lerTipoLivro();
        titulo = lerTitulo();
        autores = lerAutores();
        editora = lerEditora();
        preco = lerPreco();

        try {
            Livro livro;
            if (tipoLivro == 1) {
                double frete;
                int estoque;

                frete = lerFrete();
                estoque = lerEstoque();

                livro = new Impresso(titulo, autores, editora, preco, frete, estoque);
                livro.save();
                livro.saveSpecificDetails();
            } else if (tipoLivro == 2) {
                double tamanho;
                tamanho = lerTamanhoArquivo();

                livro = new Eletronico(titulo, autores, editora, preco, tamanho);
                livro.save();
                livro.saveSpecificDetails();
            } else {
                System.out.println("Tipo de livro inválido!");
                return;
            }

            System.out.println("Livro cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Erro ao cadastrar o livro!");
        }
    }

    public void realizarVenda() {
        System.out.print("Digite o nome do cliente: ");
        String cliente;
        cliente = lerCliente();

        Venda venda = new Venda(cliente);

        while (true) {
            System.out.print("Digite o ID do livro para adicionar à venda (0 para finalizar): ");
            int idLivro = lerID();
            if (idLivro == 0) break;

            try {
                Livro livro = buscarLivroPorId(idLivro);
                if (livro != null) {
                    venda.addLivro(livro);
                    if (livro instanceof Impresso) {
                        ((Impresso) livro).atualizarEstoque();
                    }
                    System.out.println("Livro adicionado à venda!");
                } else {
                    System.out.println("Livro não encontrado!");
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.out.println("Erro ao adicionar o livro à venda!");
            }
        }

        try {
            venda.save();
            System.out.println("Venda realizada com sucesso!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Erro ao realizar a venda!");
        }
    }

    public void listarLivros() {
        try (Connection conn = getConnection()) {
            String sql = "SELECT l.id, l.titulo, l.autores, l.editora, l.preco, l.tipo, " +
                    "i.frete, i.estoque, e.tamanho " +
                    "FROM livro l " +
                    "LEFT JOIN impresso i ON l.id = i.id " +
                    "LEFT JOIN eletronico e ON l.id = e.id";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Verifica se há algum resultado no ResultSet
            if (!rs.isBeforeFirst()) {
                System.out.println("Não há livros cadastrados.");
                return; // Sai do metodo se não houver livros
            }

            while (rs.next()) {
                String titulo, autores, editora, tipo;
                double preco, frete, tamanho;
                int id, estoque;

                id = rs.getInt("id");
                titulo = rs.getString("titulo");
                autores = rs.getString("autores");
                editora = rs.getString("editora");
                preco = rs.getDouble("preco");
                tipo = rs.getString("tipo");

                if (tipo.equals("IMPRESSO")) {
                    frete = rs.getDouble("frete");
                    estoque = rs.getInt("estoque");
                    System.out.println(new Impresso(id, titulo, autores, editora, preco, frete, estoque));
                } else if (tipo.equals("ELETRONICO")) {
                    tamanho = rs.getDouble("tamanho");
                    System.out.println(new Eletronico(id, titulo, autores, editora, preco, tamanho));
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Erro ao listar os livros!");
        }
    }

    public void listarVendas() {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM venda";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                double valor;
                String cliente;
                int id;

                id = rs.getInt("id");
                cliente = rs.getString("cliente");
                valor = rs.getDouble("valor");

                Venda venda = new Venda(cliente);
                System.out.println("Venda Nº " + id + ", Cliente: " + cliente + ", Valor Total: " + valor);

                String sqlVendaLivros = "SELECT livro_id FROM venda_livro WHERE venda_id = ?";
                PreparedStatement stmtVendaLivros = conn.prepareStatement(sqlVendaLivros);
                stmtVendaLivros.setInt(1, id);
                ResultSet rsVendaLivros = stmtVendaLivros.executeQuery();

                while (rsVendaLivros.next()) {
                    int idLivro = rsVendaLivros.getInt("livro_id");
                    Livro livro = buscarLivroPorId(idLivro);
                    if (livro != null) {
                        System.out.println("   " + livro);
                    }
                }

                stmtVendaLivros.close();
            }

            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Erro ao listar as vendas!");
        }
    }

    private Livro buscarLivroPorId(int id) throws SQLException {
        Connection conn = getConnection();
        String sql = "SELECT * FROM livro WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        Livro livro = null;
        if (rs.next()) {
            String titulo, autores, editora, tipo;
            double preco, frete, tamanho;
            int estoque;

            titulo = rs.getString("titulo");
            autores = rs.getString("autores");
            editora = rs.getString("editora");
            preco = rs.getDouble("preco");
            tipo = rs.getString("tipo");

            if (tipo.equals("IMPRESSO")) {
                String sqlImpresso = "SELECT * FROM impresso WHERE id = ?";
                PreparedStatement stmtImpresso = conn.prepareStatement(sqlImpresso);
                stmtImpresso.setInt(1, id);
                ResultSet rsImpresso = stmtImpresso.executeQuery();
                if (rsImpresso.next()) {
                    frete = rsImpresso.getDouble("frete");
                    estoque = rsImpresso.getInt("estoque");
                    livro = new Impresso(id, titulo, autores, editora, preco, frete, estoque);
                }
                stmtImpresso.close();
            } else if (tipo.equals("ELETRONICO")) {
                String sqlEletronico = "SELECT * FROM eletronico WHERE id = ?";
                PreparedStatement stmtEletronico = conn.prepareStatement(sqlEletronico);
                stmtEletronico.setInt(1, id);
                ResultSet rsEletronico = stmtEletronico.executeQuery();
                if (rsEletronico.next()) {
                    tamanho = rsEletronico.getDouble("tamanho");
                    livro = new Eletronico(id, titulo, autores, editora, preco, tamanho);
                }
                stmtEletronico.close();
            }
        }

        stmt.close();
        conn.close();

        return livro;
    }
}
