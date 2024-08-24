package input;

import java.util.Scanner;

public class EntradaDeDados {

    private static final Scanner entrada = new Scanner(System.in);

    public static int lerTipoLivro() {
        int tipoLivro;
        do {
            System.out.println("Escolha o tipo de livro:");
            System.out.println("1 - Impresso");
            System.out.println("2 - Eletrônico");
            System.out.println("3 - Sair");
            System.out.print("Digite a opção desejada: ");

            while (!entrada.hasNextInt()) {
                System.out.println("Opção inválida. Digite um número inteiro (1, 2 ou 3).");
                entrada.next(); // Limpa a entrada inválida do scanner
            }
            tipoLivro = entrada.nextInt();

            if (tipoLivro < 1 || tipoLivro > 3) {
                System.out.println("Opção inválida. Por favor, escolha 1, 2 ou 3.");
            }
        } while (tipoLivro < 1 || tipoLivro > 3);

        return tipoLivro;
    }


    public static String lerTitulo() {
        System.out.print("Digite o título: ");
        entrada.nextLine(); // Consumir o newline pendente
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
        while (true) {
            try {
                System.out.print("Digite o preço: ");
                String input = entrada.nextLine();

                // Substitui a vírgula por ponto, se necessário
                input = input.replace(",", ".");

                double preco = Double.parseDouble(input);

                // Verifica se o preço é positivo
                if (preco <= 0) {
                    System.out.println("O preço não pode ser negativo ou nulo. Tente novamente.");
                } else {
                    return preco;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número decimal (use vírgula ou ponto para separar as casas decimais).");
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
                frete = Double.parseDouble(input);

                if (frete < 0) {
                    System.out.println("O frete não pode ser negativo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Frete inválido. Digite um número decimal (use vírgula ou ponto para separar as casas decimais).");
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
                System.out.println("Estoque inválido. Digite um número inteiro.");
                entrada.next(); // Limpa a entrada inválida
            }
            estoque = entrada.nextInt();

            if (estoque < 0) {
                System.out.println("O estoque não pode ser negativo.");
            }
        } while (estoque < 0);
        return estoque;
    }

    public static long lerTamanhoArquivo() {
        long tamanho;
        do {
            System.out.print("Digite o tamanho do arquivo em KB: ");
            while (!entrada.hasNextLong()) {
                System.out.println("Tamanho inválido. Digite um número inteiro.");
                entrada.next(); // Limpa a entrada inválida
            }
            tamanho = entrada.nextLong();

            if (tamanho <= 0) {
                System.out.println("O tamanho do arquivo deve ser maior que zero.");
            }
        } while (tamanho <= 0);
        return tamanho;
    }

    public static int lerID() {
        while (true) {
            try {
                System.out.print("Digite o ID do livro para adicionar à venda (0 para finalizar): ");
                String input = entrada.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    public static int lerQuantidade() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                int quantidade = Integer.parseInt(scanner.nextLine());
                if (quantidade >= 0) {
                    return quantidade;
                } else {
                    System.out.println("Por favor, insira uma quantidade válida (0 ou mais).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número válido.");
            }
        }
    }

    public static String lerCliente() {
        System.out.print("Digite o nome do cliente: ");
        return entrada.nextLine();
    }

    public static double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Double.parseDouble(entrada.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número decimal (use ponto para separar as casas decimais).");
            }
        }
    }

    public static String lerString(String mensagem) {
        System.out.print(mensagem);
        return entrada.nextLine();
    }
}
