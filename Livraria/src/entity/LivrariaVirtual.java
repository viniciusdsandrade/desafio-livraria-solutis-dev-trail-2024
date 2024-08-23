package entity;

import java.util.Scanner;

public class LivrariaVirtual {
    private static final int MAX_IMPRESSOS = 100;
    private static final int MAX_ELETRONICOS = 100;
    private static final int MAX_VENDAS = 50;

    private final Impresso[] impressos;
    private final Eletronico[] eletronicos;
    private final Venda[] vendas;

    private int numImpressos;
    private int numEletronicos;
    private int numVendas;

    public LivrariaVirtual() {
        impressos = new Impresso[MAX_IMPRESSOS];
        eletronicos = new Eletronico[MAX_ELETRONICOS];
        vendas = new Venda[MAX_VENDAS];
        numImpressos = 0;
        numEletronicos = 0;
        numVendas = 0;
    }

    public void cadastrarLivro() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha o tipo de livro (1: Impresso, 2: Eletronico, 3: Ambos): ");
        int tipo = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (tipo == 1 || tipo == 3) {
            if (numImpressos < MAX_IMPRESSOS) {
                System.out.print("Título: ");
                String titulo = scanner.nextLine();
                System.out.print("Autores: ");
                String autores = scanner.nextLine();
                System.out.print("Editora: ");
                String editora = scanner.nextLine();
                System.out.print("Preço: ");
                double preco = scanner.nextDouble();
                System.out.print("Frete: ");
                double frete = scanner.nextDouble();
                System.out.print("Estoque: ");
                int estoque = scanner.nextInt();
                impressos[numImpressos++] = new Impresso(titulo, autores, editora, preco, frete, estoque);
            } else {
                System.out.println("Capacidade máxima de livros impressos atingida!");
            }
        }

        if (tipo == 2 || tipo == 3) {
            if (numEletronicos < MAX_ELETRONICOS) {
                System.out.print("Título: ");
                String titulo = scanner.nextLine();
                System.out.print("Autores: ");
                String autores = scanner.nextLine();
                System.out.print("Editora: ");
                String editora = scanner.nextLine();
                System.out.print("Preço: ");
                double preco = scanner.nextDouble();
                System.out.print("Tamanho (em KB): ");
                double tamanho = scanner.nextDouble();
                eletronicos[numEletronicos++] = new Eletronico(titulo, autores, editora, preco, tamanho);
            } else {
                System.out.println("Capacidade máxima de livros eletrônicos atingida!");
            }
        }
    }

    public void realizarVenda() {
        Scanner scanner = new Scanner(System.in);
        if (numVendas >= MAX_VENDAS) {
            System.out.println("Capacidade máxima de vendas atingida!");
            return;
        }

        System.out.print("Nome do cliente: ");
        String cliente = scanner.nextLine();
        System.out.print("Quantidade de livros que deseja comprar: ");
        int qtdLivros = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Venda venda = new Venda(qtdLivros, cliente);

        for (int i = 0; i < qtdLivros; i++) {
            System.out.println("Escolha o tipo de livro (1: Impresso, 2: Eletronico): ");
            int tipo = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (tipo == 1) {
                listarLivrosImpressos();
                System.out.print("Escolha o número do livro impresso: ");
                int escolha = scanner.nextInt();
                if (escolha >= 0 && escolha < numImpressos) {
                    venda.addLivro(impressos[escolha], i);
                    impressos[escolha].atualizarEstoque();
                } else {
                    System.out.println("Opção inválida!");
                }
            } else if (tipo == 2) {
                listarLivrosEletronicos();
                System.out.print("Escolha o número do livro eletrônico: ");
                int escolha = scanner.nextInt();
                if (escolha >= 0 && escolha < numEletronicos) {
                    venda.addLivro(eletronicos[escolha], i);
                } else {
                    System.out.println("Opção inválida!");
                }
            }
        }

        vendas[numVendas++] = venda;
    }

    public void listarLivrosImpressos() {
        System.out.println("Livros Impressos:");
        for (int i = 0; i < numImpressos; i++) {
            System.out.println(i + ": " + impressos[i].toString());
        }
    }

    public void listarLivrosEletronicos() {
        System.out.println("Livros Eletrônicos:");
        for (int i = 0; i < numEletronicos; i++) {
            System.out.println(i + ": " + eletronicos[i].toString());
        }
    }

    public void listarLivros() {
        listarLivrosImpressos();
        listarLivrosEletronicos();
    }

    public void listarVendas() {
        for (int i = 0; i < numVendas; i++) {
            System.out.println(vendas[i].toString());
            vendas[i].listarLivros();
        }
    }

    public static void main(String[] args) {
        LivrariaVirtual livraria = new LivrariaVirtual();
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        do {
            System.out.println("\nMenu:");
            System.out.println("1. Cadastrar Livro");
            System.out.println("2. Realizar Venda");
            System.out.println("3. Listar Livros");
            System.out.println("4. Listar Vendas");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (opcao) {
                case 1 -> livraria.cadastrarLivro();
                case 2 -> livraria.realizarVenda();
                case 3 -> livraria.listarLivros();
                case 4 -> livraria.listarVendas();
                case 5 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 5);
    }
}
