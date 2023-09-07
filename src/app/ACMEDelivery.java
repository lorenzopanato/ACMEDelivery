package app;

import modelo.Cliente;
import modelo.Entrega;
import controle.CadastroEntregas;
import controle.Clientela;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Scanner;

public class ACMEDelivery {

    private Scanner entrada;
    private PrintStream saidaPadrao = System.out;
    private Clientela clientela;
    private CadastroEntregas cadastroEntregas;

    public ACMEDelivery() {
        try {
            BufferedReader streamEntrada = new BufferedReader(new FileReader("arquivoentrada.txt"));
            entrada = new Scanner(streamEntrada);

            PrintStream streamSaida = new PrintStream(new File("arquivosaida.txt"), Charset.forName("UTF-8"));
            System.setOut(streamSaida);

            clientela = new Clientela();
            cadastroEntregas = new CadastroEntregas();

            Locale.setDefault(Locale.ENGLISH);
            entrada.useLocale(Locale.ENGLISH);
            entrada.useLocale(Locale.ENGLISH);

        } catch (IOException e) {
            System.out.println("Erro ao abrir recursos: " + e.getMessage());
        }
    }

    public void executar() {
        try {
            //executa os passos
            cadastrarCliente();
            cadastrarEntrega();
            mostrarClientesCadastrados();
            mostrarEntregasCadastradas();
            mostrarDadosCliente();
            mostrarDadosEntrega();
            mostrarDadosEntregasCliente();
            mostrarDadosEntregaDeMaiorValor();
            mostrarEnderecoDeEntrega();
            somatorioValoresDeEntregasDeCliente();

            //restaura a saida padrao e a entrada para o console
            restauraES();

            //executa o sistema de menu do usuario
            int opcao = 0;
            do {
                menu();

                //verifica se o tipo de dado é valido
                while (!entrada.hasNextInt()) {
                    System.out.println("Entrada inválida. Tente novamente.");
                    entrada.nextLine();
                }

                opcao = entrada.nextInt();
                entrada.nextLine();

                switch (opcao) {
                    case 1:
                        cadastrarClientePeloInputDoUsuario();
                        break;
                    case 2:
                        mostrarClientesEEntregas();
                        break;
                    case 0:
                        System.out.println("Encerrando a execução...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }

                if (opcao != 0) {
                    System.out.println("\nDigite 3 para voltar ao menu:");

                    while (!entrada.nextLine().equals("3")) {
                        System.out.println("Opção inválida. Tente novamente.");
                    }
                }
            } while (opcao != 0);
        } catch (Exception e) {
            System.out.println("Erro ao executar a aplicação: " + e.getMessage());
        }
        finally {
            entrada.close();
        }
    }

    private void menu() {
        System.out.println("------------------------------------------------------------");
        System.out.println("                    Menu de opções:");
        System.out.println("[1] Cadastrar um novo cliente e uma entrega correspondente");
        System.out.println("[2] Mostrar todos os clientes e entregas cadastrados");
        System.out.println("[0] Sair");
        System.out.println("------------------------------------------------------------");
    }

    //cadastrar cliente pelo input do usuario
    private void cadastrarClientePeloInputDoUsuario() {
        System.out.println("Cadastro do cliente: ");
        System.out.println("Informe o email do cliente:");
        String email = entrada.nextLine();
        System.out.println("Informe o nome do cliente:");
        String nome = entrada.nextLine();
        System.out.println("Informe o endereco do cliente:");
        String endereco = entrada.nextLine();

        var cliente = new Cliente(email, nome, endereco);

        if(!clientela.cadastraCliente(cliente))
            System.out.println("Erro: já existe um cliente com esse email no sistema");
        else {
            System.out.println("Cliente cadastrado com sucesso");
            cadastrarEntregaPeloInputDoUsuario(cliente);
        }
    }

    //cadastrar entrega pelo input do usuario
    private void cadastrarEntregaPeloInputDoUsuario(Cliente cliente) {
        System.out.println("\nCadastro da entrega: ");
        System.out.println("Informe o codigo da entrega:");
        int codigo = entrada.nextInt();
        entrada.nextLine();
        System.out.println("Informe o valor da entrega:");
        double valor = entrada.nextDouble();
        entrada.nextLine();
        System.out.println("Informe a descrição da entrega:");
        String descricao = entrada.nextLine();

        var entrega = new Entrega(codigo, valor, descricao, cliente);

        if(!cadastroEntregas.cadastraEntrega(entrega))
            System.out.println("Erro: já existe uma entrega com esse código no sistema");
        else {
            System.out.println("Entrega cadastrada com sucesso");
            cliente.adicionaEntrega(entrega);
        }
    }

    //metodo para o menu do usuario
    private void mostrarClientesEEntregas() {
        System.out.println("Clientes cadastrados: ");

        clientela.getClientela().forEach( cliente ->
                System.out.println("Cliente: " + cliente + "; Entregas: " + cliente.pesquisaEntregas()));
    }

    private void cadastrarCliente() {
        while (entrada.hasNext()) {
            String email = entrada.nextLine();

            if (email.equals("-1"))
                break;

            String nome = entrada.nextLine();
            String endereco = entrada.nextLine();

            var cliente = new Cliente(email, nome, endereco);

            //verifica se existe algum cliente no sistema com o email lido
            if(clientela.cadastraCliente(cliente))
                System.out.println("1; " + cliente);
        }
    }

    private void cadastrarEntrega() {
        while (entrada.hasNext()) {
            int codigo = entrada.nextInt();
            entrada.nextLine();

            if (codigo == -1)
                break;

            double valor = entrada.nextDouble();
            entrada.nextLine();

            String descricao = entrada.nextLine();

            String email = entrada.nextLine();

            var cliente = clientela.pesquisaCliente(email);
            var entrega = new Entrega(codigo, valor, descricao, cliente);

            //verifica se o cliente existe no sistema e se o codigo nao e repetido
            if (cliente != null && cadastroEntregas.cadastraEntrega(entrega)) {
                cliente.adicionaEntrega(entrega);
                System.out.println("2; " + entrega + "; " + cliente.getEmail());
            }
        }
    }

    private void mostrarClientesCadastrados() {
        int count = 0;

        for(Cliente c : clientela.getClientela()) {
            count++;
        }
        System.out.println("3; " + count);
    }

    private void mostrarEntregasCadastradas() {
        int count = 0;

        for(Entrega e : cadastroEntregas.getEntregas()) {
            count++;
        }
        System.out.println("4; " + count);
    }

    private void mostrarDadosCliente() {
        String email = entrada.nextLine();

        var cliente = clientela.pesquisaCliente(email);

        if (cliente == null)
            System.out.println("5; Cliente inexistente");
        else
            System.out.println("5; " + cliente);
    }

    private void mostrarDadosEntrega() {
        int codigo = entrada.nextInt();
        entrada.nextLine();

        var entrega = cadastroEntregas.pesquisaEntrega(codigo);

        if (entrega == null)
            System.out.println("6; Entrega inexistente");
        else
            System.out.println("6; " + entrega + "; " + entrega.getCliente().getEmail() + "; " + entrega.getCliente().getNome() +
                    "; " + entrega.getCliente().getEndereco());
    }

    private void mostrarDadosEntregasCliente() {
        String email = entrada.nextLine();
        var entregasCliente = cadastroEntregas.pesquisaEntrega(email);

        if (clientela.pesquisaCliente(email) != null) {
            if(clientela.pesquisaCliente(email).pesquisaEntregas().isEmpty())
                System.out.println("7; " + email + "; Nenhuma entrega para este cliente");
            else {
                entregasCliente.forEach( e -> System.out.println("7; " + email + "; " + e));
            }
        } else
            System.out.println("7; Cliente inexistente");
    }

    private void mostrarDadosEntregaDeMaiorValor() {
        var entregas = cadastroEntregas.getEntregas();
        Entrega maiorEntrega = null;

        if(!entregas.isEmpty()) {
            maiorEntrega = entregas.get(0);

            for (Entrega e : cadastroEntregas.getEntregas()) {
                if (e.getValor() > maiorEntrega.getValor())
                    maiorEntrega = e;
            }
        }
        if (maiorEntrega == null)
            System.out.println("8; Entrega inexistente");
        else
            System.out.println("8; " + maiorEntrega);
    }

    private void mostrarEnderecoDeEntrega() {
        int codigo = entrada.nextInt();
        entrada.nextLine();

        var entrega = cadastroEntregas.pesquisaEntrega(codigo);

        if (entrega == null)
            System.out.println("9; Entrega inexistente");
        else
            System.out.println("9; " + entrega + "; " + entrega.getCliente().getEndereco());
    }

    private void somatorioValoresDeEntregasDeCliente() {
        String email = entrada.nextLine();
        var cliente = clientela.pesquisaCliente(email);

        double somatorio = 0;

        if(cliente != null) {
            for (Entrega e : cliente.pesquisaEntregas()) {
                somatorio += e.getValor();
            }
        }

        if (cliente == null)
            System.out.println("10; Cliente inexistente");
        else if(cliente.pesquisaEntregas().isEmpty())
            System.out.println("10; Entrega inexistente");
        else
            System.out.printf("10; %s; %s; %.2f%n", email, cliente.getNome(), somatorio); //arredonda o somatorio para duas casas decimais
    }

    private void restauraES() {
        System.setOut(saidaPadrao);
        entrada = new Scanner(System.in);
    }
}
