package entity;

import config.DBConnection;

import java.util.Scanner;

import java.sql.*;

public class LivrariaVirtual {

    public void cadastrarLivro() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha o tipo de livro (1 - Impresso, 2 - Eletrônico): ");
        int tipoLivro = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Digite o título: ");
        String titulo = scanner.nextLine();
        System.out.print("Digite os autores: ");
        String autores = scanner.nextLine();
        System.out.print("Digite a editora: ");
        String editora = scanner.nextLine();
        System.out.print("Digite o preço: ");
        double preco = scanner.nextDouble();

        try {
            Livro livro;
            if (tipoLivro == 1) {
                System.out.print("Digite o frete: ");
                double frete = scanner.nextDouble();
                System.out.print("Digite o estoque: ");
                int estoque = scanner.nextInt();

                livro = new Impresso(titulo, autores, editora, preco, frete, estoque);
                livro.save();
                livro.saveSpecificDetails();
            } else if (tipoLivro == 2) {
                System.out.print("Digite o tamanho do arquivo em KB: ");
                double tamanho = scanner.nextDouble();

                livro = new Eletronico(titulo, autores, editora, preco, tamanho);
                livro.save();
                livro.saveSpecificDetails();
            } else {
                System.out.println("Tipo de livro inválido!");
                return;
            }

            System.out.println("Livro cadastrado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar o livro!");
        }
    }

    public void realizarVenda() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do cliente: ");
        String cliente = scanner.nextLine();

        Venda venda = new Venda(cliente);

        while (true) {
            System.out.print("Digite o ID do livro para adicionar à venda (0 para finalizar): ");
            int idLivro = scanner.nextInt();
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
                e.printStackTrace();
                System.out.println("Erro ao adicionar o livro à venda!");
            }
        }

        try {
            venda.save();
            System.out.println("Venda realizada com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao realizar a venda!");
        }
    }

    public void listarLivros() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM livro";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autores = rs.getString("autores");
                String editora = rs.getString("editora");
                double preco = rs.getDouble("preco");
                String tipo = rs.getString("tipo");

                Livro livro;
                if (tipo.equals("IMPRESSO")) {
                    String sqlImpresso = "SELECT * FROM impresso WHERE id = ?";
                    PreparedStatement stmtImpresso = conn.prepareStatement(sqlImpresso);
                    stmtImpresso.setInt(1, id);
                    ResultSet rsImpresso = stmtImpresso.executeQuery();
                    if (rsImpresso.next()) {
                        double frete = rsImpresso.getDouble("frete");
                        int estoque = rsImpresso.getInt("estoque");
                        livro = new Impresso(id, titulo, autores, editora, preco, frete, estoque);
                        System.out.println(livro);
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
                        System.out.println(livro);
                    }
                    stmtEletronico.close();
                }
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao listar os livros!");
        }
    }

    public void listarVendas() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM venda";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String cliente = rs.getString("cliente");
                double valor = rs.getDouble("valor");

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
            e.printStackTrace();
            System.out.println("Erro ao listar as vendas!");
        }
    }

    private Livro buscarLivroPorId(int id) throws SQLException {
        Connection conn = DBConnection.getConnection();
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
