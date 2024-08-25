package input;

import java.util.Scanner;

import static java.lang.Character.toUpperCase;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class EntradaDeDados {

    public static final Scanner entrada = new Scanner(System.in);

    public static char lerTipoLivroVenda() {
        char tipoLivro;
        do {

            System.out.print("""
                    Escolha o tipo de livro:
                    I - Impresso
                    E - Eletrônico
                    S - Sair
                    Digite a opção desejada:\s""");

            tipoLivro = entrada.next().charAt(0);
            tipoLivro = toUpperCase(tipoLivro);

            if (tipoLivro != 'I' && tipoLivro != 'E' && tipoLivro != 'S')
                System.err.println("Opção inválida. Por favor, escolha I, E ou S.");
        } while (tipoLivro != 'I' && tipoLivro != 'E' && tipoLivro != 'S');

        return tipoLivro;
    }

    public static int lerTipoLivroCadastro() {
        int tipoLivro;
        do {
            System.out.print("""
                    Escolha o tipo de livro:
                    1 - Impresso
                    2 - Eletrônico
                    3 - Sair
                    Digite a opção desejada:\s""");

            while (!entrada.hasNextInt()) {
                System.err.println("Opção inválida. Digite um número inteiro (1, 2 ou 3).");
                entrada.next(); // Limpa a entrada inválida do scanner
            }

            tipoLivro = entrada.nextInt();

            if (tipoLivro < 1 || tipoLivro > 3)
                System.err.println("Opção inválida. Por favor, escolha 1, 2 ou 3.");
        } while (tipoLivro < 1 || tipoLivro > 3);

        return tipoLivro;
    }

    public static String lerTitulo() {
        System.out.print("Digite o título: ");
        entrada.nextLine();
        return entrada.nextLine();
    }

    public static String lerAutores() {
        System.out.print("Digite os autores: ");
        return entrada.nextLine();
    }

    public static String lerEditora() {
        System.out.print("Digite a editora: ");
        return entrada.nextLine();
    }

    public static double lerPreco() {
        double preco;

        while (true) {
            try {
                System.out.print("Digite o preço: ");
                String input = entrada.nextLine();

                // Substitui a vírgula por ponto, se necessário
                input = input.replace(",", ".");
                preco = parseDouble(input);

                // Verifica se o preço é positivo
                if (preco <= 0)
                    System.err.println("O preço não pode ser negativo ou nulo. Tente novamente.");
                else
                    return preco;
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida. Digite um número decimal (use vírgula ou ponto para separar as casas decimais).");
            }
        }
    }

    public static double lerFrete() {
        double frete;
        do {
            System.out.print("Digite o frete: ");
            String input = entrada.next();

            // Substitui a vírgula por ponto, se necessário
            input = input.replace(",", ".");

            try {
                frete = parseDouble(input);
                if (frete < 0) System.err.println("O frete não pode ser negativo.");
            } catch (NumberFormatException e) {
                System.err.println("Frete inválido. Digite um número decimal (use vírgula ou ponto para separar as casas decimais).");
                frete = -1; // Força a repetição do loop
            }

        } while (frete < 0);
        return frete;
    }

    public static int lerEstoque() {
        int estoque;
        do {
            System.out.print("Digite o estoque: ");
            while (!entrada.hasNextInt()) {
                System.err.println("Estoque inválido. Digite um número inteiro.");
                entrada.next(); // Limpa a entrada inválida
            }

            estoque = entrada.nextInt();

            if (estoque < 0) System.err.println("O estoque não pode ser negativo.");
        } while (estoque < 0);
        return estoque;
    }

    public static long lerTamanhoArquivoEmKB() {
        long tamanho;
        do {
            System.out.print("Digite o tamanho do arquivo em KB: ");
            while (!entrada.hasNextLong()) {
                System.err.println("Tamanho inválido. Digite um número inteiro.");
                entrada.next(); // Limpa a entrada inválida
            }

            tamanho = entrada.nextLong();

            if (tamanho <= 0) System.err.println("O tamanho do arquivo deve ser maior que zero.");
        } while (tamanho <= 0);
        return tamanho;
    }

    public static int lerLivroID() {
        int idLivro = -1;
        do {
            System.out.print("Digite o ID do livro para adicionar à venda: ");
            if (entrada.hasNextInt()) {
                idLivro = entrada.nextInt();
                if (idLivro <= 0) { // Verifica se o ID é positivo
                    System.err.println("ID inválido. Digite um número inteiro positivo.");
                }
            } else {
                System.err.println("Entrada inválida. Digite um número inteiro.");
                entrada.next(); // Limpa a entrada inválida do buffer
            }
        } while (idLivro <= 0);
        return idLivro;
    }

    public static String lerCliente() {
        System.out.print("Digite o nome do cliente: ");
        return entrada.nextLine();
    }

    public static int lerQuantidadeExemplares() {
        int quantidade;
        while (true) {
            System.out.print("Digite a quantidade de exemplares: ");
            try {
                quantidade = parseInt(entrada.nextLine());
                if (quantidade >= 0) return quantidade;
                else System.err.println("Por favor, insira uma quantidade válida (0 ou mais).");
            } catch (NumberFormatException e) {
                System.err.println("Por favor, insira um número válido.");
            }
        }
    }
}
