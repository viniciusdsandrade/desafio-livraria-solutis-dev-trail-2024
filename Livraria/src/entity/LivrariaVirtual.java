package entity;

import java.sql.*;

import static config.MySQLConnection.getConnection;
import static input.EntradaDeDados.*;

public class LivrariaVirtual {

    private static final int MAX_IMPRESSOS = 100;    // Número máximo de livros impressos que podem ser cadastrados
    private static final int MAX_ELETRONICOS = 100;  // Número máximo de livros eletrônicos que podem ser cadastrados
    private static final int MAX_VENDAS = 100;       // Número máximo de vendas que podem ser cadastradas

    private final Impresso[] impressos = new Impresso[MAX_IMPRESSOS];          // Vetor de livros impressos cadastrados
    private final Eletronico[] eletronicos = new Eletronico[MAX_ELETRONICOS];  // Vetor de livros eletrônicos cadastrados
    private final Venda[] vendas = new Venda[MAX_VENDAS];                      // Vetor de vendas realizadas

    private int numImpressos = 0;       // Número de livros impressos cadastrados
    private int numEletronicos = 0;     // Número de livros eletrônicos cadastrados
    private int numVendas = 0;          // Número de vendas realizadas

    public void verificarConsistencia() {
        try (Connection conn = getConnection()) {
            // Verificar contagem de livros impressos
            String sqlImpressos = "SELECT COUNT(*) AS total FROM impresso";
            PreparedStatement stmtImpressos = conn.prepareStatement(sqlImpressos);
            ResultSet rsImpressos = stmtImpressos.executeQuery();
            if (rsImpressos.next()) numImpressos = rsImpressos.getInt("total");

            // Verificar contagem de livros eletrônicos
            String sqlEletronicos = "SELECT COUNT(*) AS total FROM eletronico";
            PreparedStatement stmtEletronicos = conn.prepareStatement(sqlEletronicos);
            ResultSet rsEletronicos = stmtEletronicos.executeQuery();
            if (rsEletronicos.next()) numEletronicos = rsEletronicos.getInt("total");

            // Verificar contagem de vendas
            String sqlVendas = "SELECT COUNT(*) AS total FROM venda";
            PreparedStatement stmtVendas = conn.prepareStatement(sqlVendas);
            ResultSet rsVendas = stmtVendas.executeQuery();
            if (rsVendas.next()) numVendas = rsVendas.getInt("total");

            System.out.println("Consistência verificada: ");
            System.out.println("Livros impressos cadastrados: " + numImpressos);
            System.out.println("Livros eletrônicos cadastrados: " + numEletronicos);
            System.out.println("Vendas realizadas: " + numVendas);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Erro ao verificar a consistência do sistema!");
        }
    }

    public void cadastrarLivro() {
        if (numImpressos >= MAX_IMPRESSOS && numEletronicos >= MAX_ELETRONICOS) {
            System.out.println("Limite de livros cadastrados atingido. Não é possível cadastrar mais livros.");
            return;
        }

        String titulo, autores, editora;
        double preco, frete;
        long tamanho;
        int tipoLivro, estoque;

        tipoLivro = lerTipoLivro();
        if (tipoLivro == 3) {
            System.out.println("Operação cancelada.");
            return;
        }

        titulo = lerTitulo();
        autores = lerAutores();
        editora = lerEditora();
        preco = lerPreco();

        try {
            Livro livro;
            if (tipoLivro == 1) {
                if (numImpressos >= MAX_IMPRESSOS) {
                    System.out.println("Limite de livros impressos cadastrados atingido.");
                    return;
                }

                frete = lerFrete();
                estoque = lerEstoque();

                livro = new Impresso(titulo, autores, editora, preco, frete, estoque);
                livro.save();
                livro.saveSpecificDetails();
                impressos[numImpressos++] = (Impresso) livro; // Atualiza o vetor de impressos

            } else if (tipoLivro == 2) {
                if (numEletronicos >= MAX_ELETRONICOS) {
                    System.out.println("Limite de livros eletrônicos cadastrados atingido.");
                    return;
                }

                tamanho = lerTamanhoArquivo();

                livro = new Eletronico(titulo, autores, editora, preco, tamanho);
                livro.save();
                livro.saveSpecificDetails();

                eletronicos[numEletronicos++] = (Eletronico) livro; // Atualiza o vetor de eletrônicos

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
        if (numVendas >= MAX_VENDAS) {
            System.out.println("Limite de vendas atingido. Não é possível realizar mais vendas.");
            return;
        }

        String cliente;
        int idLivro, quantidade;

        cliente = lerCliente();
        listarLivrosSimplificado(); // Chama o metodo para listar os livros

        Venda venda = new Venda(cliente);

        while (true) {
            idLivro = lerID();
            if (idLivro == 0) break;

            try {
                Livro livro = buscarLivroPorId(idLivro);
                if (livro != null) {
                    System.out.print("Quantos exemplares você gostaria de comprar? ");
                    quantidade = lerQuantidade();

                    if (livro instanceof Impresso && quantidade > ((Impresso) livro).getEstoque()) {
                        System.out.println("Desculpe, não há estoque suficiente.");
                    } else if (quantidade > 0) {
                        venda.addLivro(livro, quantidade); // Passa a quantidade
                        if (livro instanceof Impresso) {
                            ((Impresso) livro).atualizarEstoque(quantidade); // Passa a quantidade para atualizar o estoque
                        }
                        System.out.println("Livro adicionado à venda!");
                    } else {
                        System.out.println("Quantidade inválida.");
                    }
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
            vendas[numVendas++] = venda;
            System.out.println("Venda realizada com sucesso!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Erro ao realizar a venda!");
        }
    }

    public void listarLivrosSimplificado() {
        try (Connection conn = getConnection()) {
            String sql = """
                                        SELECT l.id, l.titulo, l.preco, \
                                        COALESCE(i.estoque, 0) AS estoque \
                                        FROM livro l \
                                        LEFT JOIN impresso i ON l.id = i.id \
                                        LEFT JOIN eletronico e ON l.id = e.id
                    """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("Não há livros cadastrados.");
                return;
            }

            System.out.println("Lista de Livros Disponíveis:");
            System.out.println("ID | Título | Preço | Estoque");
            int id, estoque;
            String titulo;
            double preco;

            while (rs.next()) {
                id = rs.getInt("id");
                titulo = rs.getString("titulo");
                preco = rs.getDouble("preco");
                estoque = rs.getInt("estoque");

                System.out.println(id + " | " + titulo + " | R$ " + preco + " | Estoque: " + estoque);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Erro ao listar os livros!");
        }
    }

    public void listarLivros() {
        try (Connection conn = getConnection()) {
            String sql = """
                                        SELECT l.id, l.titulo, l.autores, l.editora, l.preco, l.tipo, \
                                        i.frete, i.estoque, e.tamanho \
                                        FROM livro l \
                                        LEFT JOIN impresso i ON l.id = i.id \
                                        LEFT JOIN eletronico e ON l.id = e.id
                    """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Verifica se há algum resultado no ResultSet
            if (!rs.isBeforeFirst()) {
                System.out.println("Não há livros cadastrados.");
                return; // Sai do metodo se não houver livros
            }

            while (rs.next()) {
                String titulo, autores, editora, tipo;
                double preco, frete;
                int id, estoque;
                long tamanho;

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
                    tamanho = rs.getLong("tamanho");
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

            int id, idLivro, quantidade;
            String cliente;
            double valorTotal;

            while (rs.next()) {
                id = rs.getInt("id");
                cliente = rs.getString("cliente");
                valorTotal = rs.getDouble("valor");

                System.out.println("\n------------------------------");
                System.out.println("Venda Nº " + id);
                System.out.println("Cliente: " + cliente);
                System.out.println("Valor Total: R$ " + String.format("%.2f", valorTotal));
                System.out.println("Livros:");

                String sqlVendaLivros = """
                                                SELECT livro_id \
                                                FROM venda_livro \
                                                WHERE venda_id = ?
                        """;
                PreparedStatement stmtVendaLivros = conn.prepareStatement(sqlVendaLivros);
                stmtVendaLivros.setInt(1, id);
                ResultSet rsVendaLivros = stmtVendaLivros.executeQuery();

                while (rsVendaLivros.next()) {
                    idLivro = rsVendaLivros.getInt("livro_id");
                    Livro livro = buscarLivroPorId(idLivro);
                    if (livro != null) {
                        quantidade = (int) Math.round(valorTotal / livro.getPreco()); // Calcula a quantidade aproximada
                        System.out.println("   - " + livro.getTitulo() + " (R$ " + String.format("%.2f", livro.getPreco()) + " x " + quantidade + ")");
                    }
                }

                System.out.println("------------------------------\n");
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
            double preco, frete;
            int estoque;
            long tamanho;

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
                    tamanho = rsEletronico.getLong("tamanho");
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
