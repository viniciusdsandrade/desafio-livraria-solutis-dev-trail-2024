import entity.LivrariaVirtual;

import static input.EntradaDeDados.entrada;

public class Main {
    public static void main(String[] args) {
        LivrariaVirtual livraria = new LivrariaVirtual();
        int opcao;

        // Verificação de consistência ao iniciar o sistema
        livraria.verificarConsistencia();

        System.out.println("Bem-vindo à Livraria Virtual!");
        System.out.println(livraria);

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
                default -> System.out.println("Opção inválida!");
            }
        }
    }
}
