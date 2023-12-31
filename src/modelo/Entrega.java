package modelo;

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

    public int getCodigo() {
        return codigo;
    }

    public double getValor() {
        return valor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public String toString() {
        return codigo + "; " + valor + "; " + descricao;
    }
}
