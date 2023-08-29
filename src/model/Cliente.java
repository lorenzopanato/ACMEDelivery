package model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private String email;
    private String nome;
    private String endereco;
    private List<Entrega> entregas;

    public Cliente(String email, String nome, String endereco) {
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.entregas = new ArrayList<>();
    }

    public boolean adicionaEntrega(Entrega entrega) {
        return entregas.add(entrega);
    }

    public List<Entrega> pesquisaEntregas() {
        return entregas;
    }


    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                '}';
    }
}
