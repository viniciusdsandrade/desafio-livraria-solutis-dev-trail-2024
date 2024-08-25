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

    private static final int MAX_IMPRESSOS = 10;    // Número máximo de livros impressos que podem ser cadastrados
    private static final int MAX_ELETRONICOS = 20;  // Número máximo de livros eletrônicos que podem ser cadastrados
    private static final int MAX_VENDAS = 50;       // Número máximo de vendas que podem ser cadastradas

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

            StringBuilder inconsistencias = new StringBuilder();

            if (numImpressos > MAX_IMPRESSOS) inconsistencias.append("Limite de livros impressos excedido.\n");
            if (numEletronicos > MAX_ELETRONICOS) inconsistencias.append("Limite de livros eletrônicos excedido.\n");
            if (numVendas > MAX_VENDAS) inconsistencias.append("Limite de vendas excedido.\n");

            if (!inconsistencias.isEmpty()) {
                System.err.println("Inconsistência(s) detectada(s) no sistema:");
                System.err.println(inconsistencias);
            } else {
                System.out.printf("""
                        Consistência verificada:\s
                        Livros impressos cadastrados: %d\s
                        Livros eletrônicos cadastrados: %d\s
                        Vendas realizadas: %d\s
                        """, numImpressos, numEletronicos, numVendas);
            }
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

        tipoLivro = lerTipoLivroCadastro();

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

        String cliente = lerCliente();
        Venda venda = new Venda(cliente);

        int quantidade = 0;
        while (quantidade <= 0) {
            System.out.print("Quantos exemplares você gostaria de comprar? ");
            quantidade = lerQuantidade();
            if (quantidade <= 0) System.err.println("Quantidade inválida.");
        }

        char tipoLivro = lerTipoLivroVenda();

        switch (tipoLivro) {
            case 'I' -> listarLivrosImpressos();
            case 'E' -> listarLivrosEletronicos();
            default -> {
                System.err.println("Opção inválida.");
                return; // Sai da venda se a opção for inválida
            }
        }

        int idLivro = lerID();

        try {
            Livro livro = buscarLivroPorId(idLivro);
            if (livro != null) {
                if (livro instanceof Impresso && quantidade > ((Impresso) livro).getEstoque()) {
                    System.err.println("Desculpe, não há estoque suficiente.");
                    return; // Sai da venda se não houver estoque suficiente
                } else {
                    venda.addLivro(livro, quantidade);
                    if (livro instanceof Impresso)
                        ((Impresso) livro).atualizarEstoque(quantidade);
                    System.out.println("Livro adicionado à venda!");
                }
            } else {
                System.err.println("Livro não encontrado!");
                return; // Sai da venda se o livro não for encontrado
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Erro ao adicionar o livro à venda!");
            return; // Sai da venda em caso de erro
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

    public void listarLivrosImpressos() {
        try (Connection conn = getConnection()) {
            String sql = """
                        SELECT l.id, l.titulo, l.autores, l.editora, l.preco, i.frete, i.estoque \
                        FROM livro l \
                        INNER JOIN impresso i ON l.id = i.id
                    """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Verifica se há algum resultado no ResultSet
            if (!rs.isBeforeFirst()) {
                System.err.println("Não há livros impressos cadastrados.");
                return;
            }

            System.out.println("Livros Impressos:");
            System.out.println("-------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autores = rs.getString("autores");
                String editora = rs.getString("editora");
                double preco = rs.getDouble("preco");
                double frete = rs.getDouble("frete");
                int estoque = rs.getInt("estoque");

                // Utiliza o metodo toString() da classe Impresso
                Impresso impresso = new Impresso(id, titulo, autores, editora, preco, frete, estoque);
                System.out.println(impresso);
            }
            System.out.println("-------------------------------------------------------------------");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Erro ao listar os livros impressos!");
        }
    }

    public void listarLivrosEletronicos() {
        try (Connection conn = getConnection()) {
            String sql = """
                        SELECT l.id, l.titulo, l.autores, l.editora, l.preco, e.tamanho \
                        FROM livro l \
                        INNER JOIN eletronico e ON l.id = e.id
                    """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Verifica se há algum resultado no ResultSet
            if (!rs.isBeforeFirst()) {
                System.err.println("Não há livros eletrônicos cadastrados.");
                return;
            }

            System.out.println("Livros Eletrônicos:");
            System.out.println("-------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autores = rs.getString("autores");
                String editora = rs.getString("editora");
                double preco = rs.getDouble("preco");
                long tamanho = rs.getLong("tamanho");

                // Utiliza o metodo toString() da classe Eletronico
                Eletronico eletronico = new Eletronico(id, titulo, autores, editora, preco, tamanho);
                System.out.println(eletronico);
            }
            System.out.println("-------------------------------------------------------------------");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Erro ao listar os livros eletrônicos!");
        }
    }

    public void listarLivros() {
        listarLivrosImpressos();
        listarLivrosEletronicos();
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

                System.out.println("\n------------------------------------------------------------");
                System.out.println("Venda Nº " + id);
                System.out.println("Cliente: " + cliente);
                System.out.println("Valor Total: R$ " + String.format("%.2f", valorTotal));
                System.out.println("Livros:");

                String sqlVendaLivros = """
                        SELECT livro_id
                        FROM venda_livro
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

                System.out.println("------------------------------------------------------------\n");
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

    public static void main(String[] args) {
        LivrariaVirtual livraria = new LivrariaVirtual();
        int opcao;

        // Verificação de consistência ao iniciar o sistema
        livraria.verificarConsistencia();

        while (true) {
            System.out.print("""
                     \nMenu:
                     1 - Cadastrar livro
                     2 - Realizar uma venda
                     3 - Listar livros
                     4 - Listar vendas
                     0 - Sair
                    Escolha uma opção:\s""");

            opcao = entrada.nextInt();
            entrada.nextLine(); // Consume newline

            switch (opcao) {
                case 1 -> livraria.cadastrarLivro();
                case 2 -> livraria.realizarVenda();
                case 3 -> livraria.listarLivros();
                case 4 -> livraria.listarVendas();
                case 0 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.err.println("Opção inválida!");
            }
        }
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
