package entity;

import config.MysqlDBConnection;

import java.util.Scanner;

import java.sql.*;

public class LivrariaVirtual {

    private static final int MAX_IMPRESSOS = 100; // Número máximo de livros impressos que podem ser cadastrados
    private static final int MAX_ELETRONICOS = 100; // Número máximo de livros eletrônicos que podem ser cadastrados
    private static final int MAX_VENDAS = 100; // Número máximo de vendas que podem ser cadastradas

    private final Impresso[] impressos = new Impresso[MAX_IMPRESSOS]; // Vetor de livros impressos cadastrados
    private final Eletronico[] eletronicos = new Eletronico[MAX_ELETRONICOS]; // Vetor de livros eletrônicos cadastrados
    private final Venda[] vendas = new Venda[MAX_VENDAS]; // Vetor de vendas realizadas

    private int numImpressos = 0; // Número de livros impressos cadastrados
    private int numEletronicos = 0; // Número de livros eletrônicos cadastrados
    private int numVendas = 0; // Número de vendas realizadas

    public void cadastrarLivro() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha o tipo de livro (1 - Impresso, 2 - Eletrônico, 3 - Ambos): ");
        int tipoLivro = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if ((tipoLivro == 1 && numImpressos >= MAX_IMPRESSOS) ||
                (tipoLivro == 2 && numEletronicos >= MAX_ELETRONICOS) ||
                (tipoLivro == 3 && (numImpressos >= MAX_IMPRESSOS || numEletronicos >= MAX_ELETRONICOS))) {
            System.out.println("Não há espaço para cadastrar mais livros.");
            return;
        }

        System.out.print("Digite o título: ");
        String titulo = scanner.nextLine();
        System.out.print("Digite os autores: ");
        String autores = scanner.nextLine();
        System.out.print("Digite a editora: ");
        String editora = scanner.nextLine();
        System.out.print("Digite o preço: ");
        double preco = scanner.nextDouble();

        if (tipoLivro == 1 || tipoLivro == 3) {
            if (numImpressos < MAX_IMPRESSOS) {
                System.out.print("Digite o frete: ");
                double frete = scanner.nextDouble();
                System.out.print("Digite o estoque: ");
                int estoque = scanner.nextInt();
                impressos[numImpressos++] = new Impresso(titulo, autores, editora, preco, frete, estoque);
                System.out.println("Livro impresso cadastrado com sucesso!");
            }
        }

        if (tipoLivro == 2 || tipoLivro == 3) {
            if (numEletronicos < MAX_ELETRONICOS) {
                System.out.print("Digite o tamanho do arquivo em KB: ");
                double tamanho = scanner.nextDouble();
                eletronicos[numEletronicos++] = new Eletronico(titulo, autores, editora, preco, tamanho);
                System.out.println("Livro eletrônico cadastrado com sucesso!");
            }
        }
    }

    public void realizarVenda() throws SQLException {
        if (numVendas >= MAX_VENDAS) {
            System.out.println("Não há espaço para registrar mais vendas.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do cliente: ");
        String cliente = scanner.nextLine();

        Venda venda = new Venda(cliente);

        while (true) {
            System.out.println("Escolha o tipo de livro para a venda (1 - Impresso, 2 - Eletrônico, 0 - Finalizar): ");
            int tipoLivro = scanner.nextInt();

            if (tipoLivro == 0) break;

            if (tipoLivro == 1) {
                listarLivrosImpressos();
                System.out.print("Digite o ID do livro impresso para adicionar à venda: ");
                int idLivro = scanner.nextInt();
                if (idLivro >= 0 && idLivro < numImpressos) {
                    venda.addLivro(impressos[idLivro]);
                    impressos[idLivro].atualizarEstoque();
                    System.out.println("Livro impresso adicionado à venda!");
                } else {
                    System.out.println("Livro impresso não encontrado!");
                }
            } else if (tipoLivro == 2) {
                listarLivrosEletronicos();
                System.out.print("Digite o ID do livro eletrônico para adicionar à venda: ");
                int idLivro = scanner.nextInt();
                if (idLivro >= 0 && idLivro < numEletronicos) {
                    venda.addLivro(eletronicos[idLivro]);
                    System.out.println("Livro eletrônico adicionado à venda!");
                } else {
                    System.out.println("Livro eletrônico não encontrado!");
                }
            } else {
                System.out.println("Tipo de livro inválido!");
            }
        }

        vendas[numVendas++] = venda;
        System.out.println("Venda realizada com sucesso!");
    }

    public void listarLivrosImpressos() {
        System.out.println("Lista de Livros Impressos:");
        for (int i = 0; i < numImpressos; i++) {
            System.out.println(impressos[i]);
        }
    }

    public void listarLivrosEletronicos() {
        System.out.println("Lista de Livros Eletrônicos:");
        for (int i = 0; i < numEletronicos; i++) {
            System.out.println(eletronicos[i]);
        }
    }

    public void listarLivros() {
        listarLivrosImpressos();
        listarLivrosEletronicos();
    }

    public void listarVendas() {
        System.out.println("Lista de Vendas:");
        for (int i = 0; i < numVendas; i++) {
            System.out.println("Venda Nº " + (i + 1) + ": " + vendas[i]);
        }
    }

    private Livro buscarLivroPorId(int id) throws SQLException {
        Connection conn = MysqlDBConnection.getConnection();
        String sql = "SELECT * FROM livro WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        Livro livro = null;
        if (rs.next()) {
            String titulo = rs.getString("titulo");
            String autores = rs.getString("autores");
            String editora = rs.getString("editora");
            double preco = rs.getDouble("preco");
            String tipo = rs.getString("tipo");

            if (tipo.equals("IMPRESSO")) {
                String sqlImpresso = "SELECT * FROM impresso WHERE id = ?";
                PreparedStatement stmtImpresso = conn.prepareStatement(sqlImpresso);
                stmtImpresso.setInt(1, id);
                ResultSet rsImpresso = stmtImpresso.executeQuery();
                if (rsImpresso.next()) {
                    double frete = rsImpresso.getDouble("frete");
                    int estoque = rsImpresso.getInt("estoque");
                    livro = new Impresso(id, titulo, autores, editora, preco, frete, estoque);
                }
                stmtImpresso.close();
            } else if (tipo.equals("ELETRONICO")) {
                String sqlEletronico = "SELECT * FROM eletronico WHERE id = ?";
                PreparedStatement stmtEletronico = conn.prepareStatement(sqlEletronico);
                stmtEletronico.setInt(1, id);
                ResultSet rsEletronico = stmtEletronico.executeQuery();
                if (rsEletronico.next()) {
                    double tamanho = rsEletronico.getDouble("tamanho");
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
