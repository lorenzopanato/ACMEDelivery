package app;

import model.Cliente;
import service.CadastroEntregas;
import service.Clientela;

import java.io.*;
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

        } catch (IOException e) {
            System.out.println("Erro no arquivo: " + e.getMessage());
        }
    }

    public void executar() {
        cadastrarCliente();

        try {
            entrada.close();
            saida.close();
        } catch (IOException e) {
            System.out.println("Erro ao fechar o output: " + e.getMessage());
        }
    }

    public void cadastrarCliente() {
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
            } catch (IOException e) {
                System.out.println("Erro ao escrever no arquivo de saída: " + e.getMessage());
            }
        }
    }

}
