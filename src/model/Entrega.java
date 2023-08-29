package model;

public class Entrega {

    private int codigo;
    private double valor;
    private String descricao;
    private Cliente cliente;

    public Entrega(int codigo, double valor, String descricao, Cliente cliente) {
        this.codigo = codigo;
        this.valor = valor;
        this.descricao = descricao;
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Entrega{" +
                "codigo=" + codigo +
                ", valor=" + valor +
                ", descricao='" + descricao + '\'' +
                ", cliente=" + cliente +
                '}';
    }
}
