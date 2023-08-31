package app;

import model.Cliente;
import model.Entrega;
import service.CadastroEntregas;
import service.Clientela;

import java.io.*;
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
        cadastrarCliente();
        cadastrarEntrega();
        mostrarClientesCadastrados();
        mostrarEntregasCadastradas();

        try {
            entrada.close();
            saida.close();
        } catch (Exception e) {
            System.out.println("Erro ao fechar o output: " + e.getMessage());
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
                if(clientela.cadastraCliente(cliente))
                    saida.write("1; " + cliente + "\n");
            } catch (Exception e) {
                System.out.println("Erro ao escrever no arquivo de saída: " + e.getMessage());
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
                if (cadastroEntregas.cadastraEntrega(entrega) && cliente != null)
                    saida.write("2; " + entrega + "\n");
            } catch (Exception e) {
                System.out.println("Erro ao escrever no arquivo de saída: " + e.getMessage());
            }
        }
    }

    public void mostrarClientesCadastrados() {
        int count = 0;

        for(Cliente c : clientela.getClientela()) {
            count++;
        }

        try {
            saida.write("3; " + count + "\n");
        } catch (Exception e) {
            System.out.println("Erro ao escrever no arquivo de saída: " + e.getMessage());
        }
    }

    public void mostrarEntregasCadastradas() {
        int count = 0;

        for(Entrega e : cadastroEntregas.getEntregas()) {
            count++;
        }

        try {
            saida.write("4; " + count + "\n");
        } catch (Exception e) {
            System.out.println("Erro ao escrever no arquivo de saída: " + e.getMessage());
        }
    }
}
