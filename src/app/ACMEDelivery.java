package app;

import java.io.*;
import java.util.Scanner;

public class ACMEDelivery {

    private Scanner entrada;
    private BufferedWriter saida;

    public ACMEDelivery() {
        try {
            BufferedReader streamEntrada = new BufferedReader(new FileReader("arquivoentrada.txt"));
            entrada = new Scanner(streamEntrada);
            saida = new BufferedWriter(new FileWriter("arquivosaida.txt"));

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void executar() {

    }


}
