import service.CadastroEntregas;
import service.Clientela;
import model.Cliente;
import model.Entrega;

public class Main {
    public static void main(String[] args) {

        //teste
        Cliente cliente = new Cliente("lorenzohpanato@gmail.com", "Lorenzo", "rua 123");
        Cliente cliente1 = new Cliente("lorenzohato@gmail.com", "a", "rua 2");
        Entrega entrega = new Entrega(123, 120.00, "entrega", cliente);

        Clientela clientela = new Clientela();
        CadastroEntregas cadastroEntregas = new CadastroEntregas();

        System.out.println(clientela.cadastraCliente(cliente));
        System.out.println(clientela.cadastraCliente(cliente1));

        cliente.adicionaEntrega(entrega);
        System.out.println(cliente.adicionaEntrega(entrega));
        cliente.pesquisaEntregas().forEach(e -> System.out.println(e));
    }
}