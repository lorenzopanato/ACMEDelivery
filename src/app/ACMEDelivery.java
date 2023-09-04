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

    // input/output de arquivos
    private Scanner entradaArquivo;
    private PrintWriter saida;

    //input do menu do usuario
    private Scanner entradaUsuario;

    private Clientela clientela;
    private CadastroEntregas cadastroEntregas;

    public ACMEDelivery() {
        try {
            BufferedReader streamEntrada = new BufferedReader(new FileReader("arquivoentrada.txt"));
            entradaArquivo = new Scanner(streamEntrada);
            saida = new PrintWriter("arquivosaida.txt");
            entradaUsuario = new Scanner(System.in);
            clientela = new Clientela();
            cadastroEntregas = new CadastroEntregas();

            Locale.setDefault(Locale.ENGLISH);
            entradaArquivo.useLocale(Locale.ENGLISH);
            entradaUsuario.useLocale(Locale.ENGLISH);

        } catch (IOException e) {
            System.out.println("Erro ao abrir recursos: " + e.getMessage());
        }
    }

    public void executar() {
        try {
            //executa metodos de leitura e escrita de arquivos
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
                        cadastrarClienteUsuario();
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
            } while (opcao != 0);
        } catch (Exception e) {
            System.out.println("Erro ao executar a aplicação: " + e.getMessage());
        }
        finally {
            entradaArquivo.close();
            saida.close();
            entradaUsuario.close();
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
    private void cadastrarClienteUsuario() {
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

    private void mostrarClientesEEntregas() {
        saida.println();
        saida.println("Clientes cadastrados: ");
        clientela.getClientela().forEach( cliente ->
                saida.println("Cliente: " + cliente + "; Entregas: " + cliente.pesquisaEntregas()));

        //adiciona as informacoes no arquivo de saida em tempo real
        saida.flush();
    }

    private void cadastrarCliente() {
        while (entradaArquivo.hasNext()) {
            String email = entradaArquivo.nextLine();

            if (email.equals("-1"))
                break;

            String nome = entradaArquivo.nextLine();
            String endereco = entradaArquivo.nextLine();

            Cliente cliente = new Cliente(email, nome, endereco);

            //verifica se existe algum cliente no sistema com o email lido
            if(clientela.cadastraCliente(cliente))
                saida.println("1; " + cliente);
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

            //verifica se o cliente existe no sistema e se o codigo nao e repetido
            if (cliente != null && cadastroEntregas.cadastraEntrega(entrega)) {
                cliente.adicionaEntrega(entrega);
                saida.println("2; " + entrega + "; " + cliente.getEmail());
            }
        }
    }

    private void mostrarClientesCadastrados() {
        int count = 0;

        for(Cliente c : clientela.getClientela()) {
            count++;
        }

        saida.println("3; " + count);
    }

    private void mostrarEntregasCadastradas() {
        int count = 0;

        for(Entrega e : cadastroEntregas.getEntregas()) {
            count++;
        }

        saida.println("4; " + count);
    }

    private void mostrarDadosCliente() {
        String email = entradaArquivo.nextLine();

        Cliente cliente = clientela.pesquisaCliente(email);

        if (cliente == null)
            saida.println("5; Cliente inexistente");
        else
            saida.println("5; " + cliente);
    }

    private void mostrarDadosEntrega() {
        int codigo = entradaArquivo.nextInt();
        entradaArquivo.nextLine();

        Entrega entrega = cadastroEntregas.pesquisaEntrega(codigo);

        if (entrega == null)
            saida.println("6; Entrega inexistente");
        else
            saida.println("6; " + entrega + "; " + entrega.getCliente().getEmail() + "; " + entrega.getCliente().getNome() +
                    "; " + entrega.getCliente().getEndereco());
    }

    private void mostrarDadosEntregasCliente() {
        String email = entradaArquivo.nextLine();
        var entregasCliente = cadastroEntregas.pesquisaEntrega(email);

        if (clientela.pesquisaCliente(email) != null) {
            //verifica se o cliente nao possui entregas
            if(clientela.pesquisaCliente(email).pesquisaEntregas().isEmpty())
                saida.println("7; " + email + "; Nenhuma entrega para este cliente");
            else {
                //percorre as entregas do cliente, mostrando uma por uma no output
                for (int i = 0; i < entregasCliente.size(); i++) {
                    saida.println("7; " + email + "; " + entregasCliente.get(i));
                }
            }
        } else
            saida.println("7; Cliente inexistente");
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

        if (maiorEntrega == null)
            saida.println("8; Entrega inexistente");
        else
            saida.println("8; " + maiorEntrega);
    }

    private void mostrarEnderecoDeEntrega() {
        int codigo = entradaArquivo.nextInt();
        entradaArquivo.nextLine();

        Entrega entrega = cadastroEntregas.pesquisaEntrega(codigo);

        if (entrega == null)
            saida.println("9; Entrega inexistente");
        else
            saida.println("9; " + entrega + "; " + entrega.getCliente().getEndereco());
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

        if (cliente == null)
            saida.println("10; Cliente inexistente");
        else if(cliente.pesquisaEntregas().isEmpty())
            saida.println("10; Entrega inexistente");
        else                                                                    //arredonda o somatorio para duas casas decimais
            saida.println("10; " + email + "; " + cliente.getNome() + "; " + new DecimalFormat("#.00").format(somatorio));
    }
}
