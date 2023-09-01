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

    private Scanner entrada;
    private BufferedWriter saida;
    private Clientela clientela;
    private CadastroEntregas cadastroEntregas;

    public ACMEDelivery() {
        try {
            clientela = new Clientela();
            cadastroEntregas = new CadastroEntregas();

            BufferedReader streamEntrada = new BufferedReader(new FileReader("arquivoentrada.txt"));
            entrada = new Scanner(streamEntrada);
            saida = new BufferedWriter(new FileWriter("arquivosaida.txt"));

            Locale.setDefault(Locale.ENGLISH);
            entrada.useLocale(Locale.ENGLISH);

        } catch (Exception e) {
            System.out.println("Erro no arquivo: " + e.getMessage());
        }
    }

    public void executar() {
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
                entrada.close();
                saida.close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar o output: " + e.getMessage());
            }
        }
    }

    private void cadastrarCliente() {
        while (entrada.hasNext()) {
            String email = entrada.nextLine();

            if (email.equals("-1"))
                break;

            String nome = entrada.nextLine();
            String endereco = entrada.nextLine();

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
        while (entrada.hasNext()) {
            int codigo = entrada.nextInt();
            entrada.nextLine();

            if (codigo == -1)
                break;

            double valor = entrada.nextDouble();
            entrada.nextLine();

            String descricao = entrada.nextLine();

            String email = entrada.nextLine();

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
        String email = entrada.nextLine();

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
        int codigo = entrada.nextInt();
        entrada.nextLine();

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
        String email = entrada.nextLine();
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
        int codigo = entrada.nextInt();
        entrada.nextLine();

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
        String email = entrada.nextLine();
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
