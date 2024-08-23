import entity.LivrariaVirtual;

import java.util.Scanner;

public class Main {
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
