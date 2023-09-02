package app;

import model.Cliente;
import model.Entrega;
import service.CadastroEntregas;
import service.Clientela;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Scanner;

public class ACMEDelivery {

    private Scanner entradaArquivo;
    private Scanner entradaUsuario;
    private BufferedWriter saida;
    private Clientela clientela;
    private CadastroEntregas cadastroEntregas;

    public ACMEDelivery() {
        try {
            BufferedReader streamEntrada = new BufferedReader(new FileReader("arquivoentrada.txt"));
            entradaArquivo = new Scanner(streamEntrada);
            saida = new BufferedWriter(new FileWriter("arquivosaida.txt"));
            entradaUsuario = new Scanner(System.in);
            clientela = new Clientela();
            cadastroEntregas = new CadastroEntregas();

            Locale.setDefault(Locale.ENGLISH);
            entradaArquivo.useLocale(Locale.ENGLISH);
            entradaUsuario.useLocale(Locale.ENGLISH);

        } catch (Exception e) {
            System.out.println("Erro no arquivo: " + e.getMessage());
        }
    }

    public void executar() {
        //executa a leitura e escrita dos arquivos
        try {
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
        } catch (Exception e) {
            System.out.println("Erro ao executar a aplicação: " + e.getMessage());
        } finally {
            try {
                entradaArquivo.close();
                saida.close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar o output/input: " + e.getMessage());
            }
        }

        //executa o sistema de menu do usuario
        int opcao = 0;
        do {
            menu();

            //verifica se o tipo de dado é valido
            while (!entradaUsuario.hasNextInt()) {
                System.out.println("Entrada inválida. Tente novamente.");
                entradaUsuario.nextLine();
            }

            opcao = entradaUsuario.nextInt();
            entradaUsuario.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarClienteEEntregaUsuario();
                    break;
                case 2:
                    mostrarClientesEEntregasUsuario();
                    break;
                case 0:
                    System.out.println("Encerrando a execução...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        } while (opcao != 0);

        entradaUsuario.close();
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
    private void cadastrarClienteEEntregaUsuario() {
        System.out.println("Cadastro do cliente: ");
        System.out.println("Informe o email do cliente:");
        String email = entradaUsuario.nextLine();
        System.out.println("Informe o nome do cliente:");
        String nome = entradaUsuario.nextLine();
        System.out.println("Informe o endereco do cliente:");
        String endereco = entradaUsuario.nextLine();

        var cliente = new Cliente(email, nome, endereco);

        clientela.cadastraCliente(cliente);
        cadastrarEntregaUsuario(cliente);
    }

    //cadastrar entrega pelo input do usuario
    private void cadastrarEntregaUsuario(Cliente cliente) {
        System.out.println("Cadastro da entrega: ");
        System.out.println("Informe o codigo da entrega:");
        int codigo = entradaUsuario.nextInt();
        entradaUsuario.nextLine();
        System.out.println("Informe o valor da entrega:");
        double valor = entradaUsuario.nextDouble();
        entradaUsuario.nextLine();
        System.out.println("Informe a descrição da entrega:");
        String descricao = entradaUsuario.nextLine();

        var entrega = new Entrega(codigo, valor, descricao, cliente);

        cadastroEntregas.cadastraEntrega(entrega);
        cliente.adicionaEntrega(entrega);
    }

    private void mostrarClientesEEntregasUsuario() {
        System.out.println("Clientes cadastrados: ");
        clientela.getClientela().forEach( cliente -> System.out.println("Cliente: " + cliente +
                "; Entregas: " + cliente.pesquisaEntregas()));
    }

    private void cadastrarCliente() {
        while (entradaArquivo.hasNext()) {
            String email = entradaArquivo.nextLine();

            if (email.equals("-1"))
                break;

            String nome = entradaArquivo.nextLine();
            String endereco = entradaArquivo.nextLine();

            Cliente cliente = new Cliente(email, nome, endereco);

            try {
                //verifica se existe algum cliente no sistema com o email lido
                if(clientela.cadastraCliente(cliente))
                    saida.write("1; " + cliente + "\n");
            } catch (IOException e) {
                System.out.println("Erro ao escrever no arquivo de saída (passo 1): " + e.getMessage());
            }
        }
    }

    private void cadastrarEntrega() {
        while (entradaArquivo.hasNext()) {
            int codigo = entradaArquivo.nextInt();
            entradaArquivo.nextLine();

            if (codigo == -1)
                break;

            double valor = entradaArquivo.nextDouble();
            entradaArquivo.nextLine();

            String descricao = entradaArquivo.nextLine();

            String email = entradaArquivo.nextLine();

            Cliente cliente = clientela.pesquisaCliente(email);
            Entrega entrega = new Entrega(codigo, valor, descricao, cliente);

            try {
                //verifica se o cliente existe no sistema e se o codigo nao e repetido
                if (cliente != null && cadastroEntregas.cadastraEntrega(entrega)) {
                    cliente.adicionaEntrega(entrega);
                    saida.write("2; " + entrega + "; " + cliente.getEmail() + "\n");
                }
            } catch (IOException e) {
                System.out.println("Erro ao escrever no arquivo de saída (passo 2): " + e.getMessage());
            }
        }
    }

    private void mostrarClientesCadastrados() {
        int count = 0;

        for(Cliente c : clientela.getClientela()) {
            count++;
        }

        try {
            saida.write("3; " + count + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo de saída (passo 3): " + e.getMessage());
        }
    }

    private void mostrarEntregasCadastradas() {
        int count = 0;

        for(Entrega e : cadastroEntregas.getEntregas()) {
            count++;
        }

        try {
            saida.write("4; " + count + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo de saída (passo 4): " + e.getMessage());
        }
    }

    private void mostrarDadosCliente() {
        String email = entradaArquivo.nextLine();

        Cliente cliente = clientela.pesquisaCliente(email);

        try {
            if (cliente == null)
                saida.write("5; Cliente inexistente\n");
            else
                saida.write("5; " + cliente + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo de saída (passo 5): " + e.getMessage());
        }
    }

    private void mostrarDadosEntrega() {
        int codigo = entradaArquivo.nextInt();
        entradaArquivo.nextLine();

        Entrega entrega = cadastroEntregas.pesquisaEntrega(codigo);

        try {
            if (entrega == null)
                saida.write("6; Entrega inexistente\n");
            else
                saida.write("6; " + entrega + "; " + entrega.getCliente().getEmail() + "; " + entrega.getCliente().getNome() +
                        "; " + entrega.getCliente().getEndereco() + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo de saída (passo 6): " + e.getMessage());
        }
    }

    private void mostrarDadosEntregasCliente() {
        String email = entradaArquivo.nextLine();
        var entregasCliente = cadastroEntregas.pesquisaEntrega(email);

        try {
            if (clientela.pesquisaCliente(email) != null) {
                //verifica se o cliente nao possui entregas
                if(clientela.pesquisaCliente(email).pesquisaEntregas().isEmpty())
                    saida.write("7; " + email + "; Nenhuma entrega para este cliente\n");
                else {
                    //percorre as entregas do cliente, mostrando uma por uma no output
                    for (int i = 0; i < entregasCliente.size(); i++) {
                        saida.write("7; " + email + "; " + entregasCliente.get(i) + "\n");
                    }
                }
            } else
                saida.write("7; Cliente inexistente\n");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo de saída (passo 7): " + e.getMessage());
        }
    }

    private void mostrarDadosEntregaDeMaiorValor() {
        var entregas = cadastroEntregas.getEntregas();
        Entrega maiorEntrega = null;

        //verifica se existem entregas no sistema
        if(!entregas.isEmpty()) {
            maiorEntrega = entregas.get(0);

            for (Entrega e : cadastroEntregas.getEntregas()) {
                if (e.getValor() > maiorEntrega.getValor())
                    maiorEntrega = e;
            }
        }

        try {
            if (maiorEntrega == null)
                saida.write("8; Entrega inexistente\n");
            else
                saida.write("8; " + maiorEntrega + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo de saída (passo 8): " + e.getMessage() + "\n");
        }
    }

    private void mostrarEnderecoDeEntrega() {
        int codigo = entradaArquivo.nextInt();
        entradaArquivo.nextLine();

        Entrega entrega = cadastroEntregas.pesquisaEntrega(codigo);

        try {
            if (entrega == null)
                saida.write("9; Entrega inexistente\n");
            else
                saida.write("9; " + entrega + "; " + entrega.getCliente().getEndereco() + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo de saída (passo 9): " + e.getMessage() + "\n");
        }
    }

    private void somatorioValoresDeEntregasDeCliente() {
        String email = entradaArquivo.nextLine();
        Cliente cliente = clientela.pesquisaCliente(email);

        double somatorio = 0;

        if(cliente != null) {
            for (Entrega e : cliente.pesquisaEntregas()) {
                somatorio += e.getValor();
            }
        }

        try {
            if (cliente == null)
                saida.write("10; Cliente inexistente");
            else if(cliente.pesquisaEntregas().isEmpty())
                saida.write("10; Entrega inexistente");
            else                                                                    //arredonda o somatorio para duas casas decimais
                saida.write("10; " + email + "; " + cliente.getNome() + "; " + new DecimalFormat("#.00").format(somatorio));
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo de saída (passo 10): " + e.getMessage() + "\n");
        }
    }
}
