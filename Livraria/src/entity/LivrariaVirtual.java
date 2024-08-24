package entity;

import java.sql.*;

import static config.MySQLConnection.getConnection;
import static input.EntradaDeDados.*;

/*
    3.5 LivrariaVirtual
    A classe LivrariaVitual possui 9 atributos:
            a) MAX_IMPRESSOS: constante que representa o número máximode livros impressos que podem ser cadastrados;
            b) MAX_ELETRONICOS: constante que representa o número máximo de livros eletrônicos que podem ser cadastrados;
            c) MAX_VENDAS: constante que representa o número máximo de vendas que podem ser cadastradas;
            d) impressos: vetor de referências a objetos da classe Impresso, representa os livros impressos cadastrados;
            e) eletronicos: vetor de referências a objetos da classe Eletronico, representa os livros eletrônicos cadastrados;
            f) vendas: vetor de referências a objetos da classe Venda, representa as vendas realizadas;
            g) numImpressos: número de livros impressos cadastrados;
            h) numEletronicos: número de livros eletrônicos cadastrados;
            i) numVendas: número de vendas realizadas.

    A seguir são descritos os métodos da classe LivrariaVirtual:

        a) cadastrarLivro(): este metodo é invocado quando a primeira opção
        do menu do sistema (Cadastrar livro) for selecionada. O usuário deve
        informar o tipo de livro que será cadastrado: impresso, eletrônico ou
        ambos. Depois o sistema deve solicitar os dados do tipo de livro
        escolhido (ou de ambos). Se não houver mais espaço no vetor para
        cadastrar um novo livro, o sistema deve exibir uma mensagem;

        b) realizarVenda(): este metodo é invocado quando a segunda opção
        do menu do sistema (Realizar uma venda) é selecionada. O sistema
        deve solicitar o nome do cliente e a quantidade de livros que ele
        deseja comprar. Depois, para cada livro, o sistema deve solicitar seu
        tipo (impresso ou eletrônico), exibir a lista de livros do tipo escolhido e
        permitir que o usuário escolha um dos livros dessa lista. Utilize os
        métodos listarLivrosImpressos() e listarLivrosEletronicos()
        descritos a seguir;

        c) listarLivrosImpressos(): exibe no vídeo no formato de tabela os
        dados de todos os livros impressos cadastrados. Utilize o metodo
        toString() da classe Impresso;

        d) listarLivrosEletronicos(): exibe no vídeo no formato de tabela os
        dados de todos os livros eletrônicos cadastrados. Utilize o metodo
        toString() da classe Eletronico;

        e) listarLivros(): este metodo é invocado quando a terceira opção do
        menu do sistema (Listar livros) é selecionada. O metodo exibe no
        vídeo os dados de todos os livros impressos e eletrônicos
        cadastrados. Utilize os métodos listarLivrosImpressos() e
        listarLivrosEletronicos();

        f) listarVendas(): este metodo é invocado quando a quarta opção do
        menu do sistema (Listar vendas) é selecionada. O metodo exibe no
        vídeo os dados de todas as vendas realizadas;

        g) main(args: String[]): este metodo deve instanciar um objeto da
        classe LivrariaVirtual, exibir repetidamente o menu de opções e
        invocar os métodos apropriados a partir da seleção do usuário.
 */
public class LivrariaVirtual {

    private static final int MAX_IMPRESSOS = 100;    // Número máximo de livros impressos que podem ser cadastrados
    private static final int MAX_ELETRONICOS = 100;  // Número máximo de livros eletrônicos que podem ser cadastrados
    private static final int MAX_VENDAS = 100;       // Número máximo de vendas que podem ser cadastradas

    private Impresso[] impressos = new Impresso[MAX_IMPRESSOS];          // Vetor de livros impressos cadastrados
    private Eletronico[] eletronicos = new Eletronico[MAX_ELETRONICOS];  // Vetor de livros eletrônicos cadastrados
    private Venda[] vendas = new Venda[MAX_VENDAS];                      // Vetor de vendas realizadas

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

            System.out.print("""
                    Consistência verificada:
                    Livros impressos cadastrados:\s""" + numImpressos + """
                    Livros eletrônicos cadastrados:\s""" + numEletronicos + """
                    Vendas realizadas:\s""" + numVendas + """
                    """);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Erro ao verificar a consistência do sistema!");
        }
    }

    public void cadastrarLivro() {
        if (numImpressos >= MAX_IMPRESSOS && numEletronicos >= MAX_ELETRONICOS) {
            System.err.println("Limite de livros cadastrados atingido. Não é possível cadastrar mais livros.");
            return;
        }

        String titulo, autores, editora;
        int tipoLivro, estoque;
        double preco, frete;
        long tamanho;

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
                    System.err.println("Limite de livros impressos cadastrados atingido.");
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
                    System.err.println("Limite de livros eletrônicos cadastrados atingido.");
                    return;
                }

                tamanho = lerTamanhoArquivo();

                livro = new Eletronico(titulo, autores, editora, preco, tamanho);
                livro.save();
                livro.saveSpecificDetails();

                eletronicos[numEletronicos++] = (Eletronico) livro; // Atualiza o vetor de eletrônicos
            } else {
                System.err.println("Tipo de livro inválido!");
                return;
            }

            System.out.println("Livro cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Erro ao cadastrar o livro!");
        }
    }

    public void realizarVenda() {
        if (numVendas >= MAX_VENDAS) {
            System.err.println("Limite de vendas atingido. Não é possível realizar mais vendas.");
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
                        System.err.println("Desculpe, não há estoque suficiente.");
                    } else if (quantidade > 0) {
                        venda.addLivro(livro, quantidade); // Passa a quantidade
                        if (livro instanceof Impresso) {
                            ((Impresso) livro).atualizarEstoque(quantidade); // Passa a quantidade para atualizar o estoque
                        }
                        System.out.println("Livro adicionado à venda!");
                    } else {
                        System.err.println("Quantidade inválida.");
                    }
                } else {
                    System.err.println("Livro não encontrado!");
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.err.println("Erro ao adicionar o livro à venda!");
            }
        }

        try {
            venda.save();
            vendas[numVendas++] = venda;
            System.out.println("Venda realizada com sucesso!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Erro ao realizar a venda!");
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
                System.err.println("Não há livros cadastrados.");
                return;
            }

            System.out.println("Lista de Livros Disponíveis:");
            System.out.println("-------------------------------------------------------------------");
            System.out.printf("%-5s | %-30s | %-10s | %-10s\n", "ID", "Título", "Preço", "Estoque");
            System.out.println("-------------------------------------------------------------------");

            int id, estoque;
            String titulo;
            double preco;

            while (rs.next()) {
                id = rs.getInt("id");
                titulo = rs.getString("titulo");
                preco = rs.getDouble("preco");
                estoque = rs.getInt("estoque");

                // Impressão formatada dos dados
                System.out.printf("%-5d | %-30s | R$ %-8.2f | %-10d\n", id, titulo, preco, estoque);
            }
            System.out.println("-------------------------------------------------------------------");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Erro ao listar os livros!");
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
                System.err.println("Não há livros cadastrados.");
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
            System.err.println("Erro ao listar os livros!");
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
                System.out.print("""
                        Venda Nº\s""" + id + """
                        Cliente:\s""" + cliente + """
                        Valor Total: R$\s""" + String.format("%.2f", valorTotal) + """
                        Livros:\s""");

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
            System.err.println("Erro ao listar as vendas!");
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

    public static int getMaxImpressos() {
        return MAX_IMPRESSOS;
    }

    public static int getMaxEletronicos() {
        return MAX_ELETRONICOS;
    }

    public static int getMaxVendas() {
        return MAX_VENDAS;
    }

    public Eletronico[] getEletronicos() {
        return eletronicos;
    }

    public Impresso[] getImpressos() {
        return impressos;
    }

    public Venda[] getVendas() {
        return vendas;
    }

    public int getNumEletronicos() {
        return numEletronicos;
    }

    public int getNumImpressos() {
        return numImpressos;
    }

    public int getNumVendas() {
        return numVendas;
    }

    public void setImpressos(Impresso[] impressos) {
        this.impressos = impressos;
    }

    public void setVendas(Venda[] vendas) {
        this.vendas = vendas;
    }

    public void setEletronicos(Eletronico[] eletronicos) {
        this.eletronicos = eletronicos;
    }

    public void setNumImpressos(int numImpressos) {
        this.numImpressos = numImpressos;
    }

    public void setNumEletronicos(int numEletronicos) {
        this.numEletronicos = numEletronicos;
    }

    public void setNumVendas(int numVendas) {
        this.numVendas = numVendas;
    }
}
