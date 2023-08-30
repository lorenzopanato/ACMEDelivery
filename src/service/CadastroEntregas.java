package service;

import model.Cliente;
import model.Entrega;

import java.util.ArrayList;
import java.util.List;

public class CadastroEntregas {

    private List<Entrega> entregas;
    private Clientela clientela;

    public CadastroEntregas() {
        entregas = new ArrayList<>();
    }

    public boolean cadastraEntrega(Entrega entrega) {
        if(pesquisaEntrega(entrega.getCodigo()) != null)
            return false;

        return entregas.add(entrega);
    }

    public Entrega pesquisaEntrega(int codigo) {
        for(Entrega e : entregas) {
            if(e.getCodigo() == codigo)
                return e;
        }
        return null;
    }

    public List<Entrega> pesquisaEntrega(String email) {
        Cliente cliente = clientela.pesquisaCliente(email);

        if(cliente != null)
            return cliente.pesquisaEntregas();

        return null;
    }
}
