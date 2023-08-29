import model.Cliente;
import model.Entrega;

public class Main {
    public static void main(String[] args) {

        //teste
        Cliente cliente = new Cliente("lorenzohpanato@gmail.com", "Lorenzo", "rua 123");
        Entrega entrega = new Entrega(123, 120.00, "entrega", cliente);

        cliente.adicionaEntrega(entrega);
        cliente.pesquisaEntregas().forEach(e -> System.out.println(e));
    }
}