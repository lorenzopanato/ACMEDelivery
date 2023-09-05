package controle;

import modelo.Entrega;

import java.util.ArrayList;
import java.util.List;

public class CadastroEntregas {

    private List<Entrega> entregas;

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
        for(Entrega e : entregas) {
            if(e.getCliente().getEmail().equals(email))
                return e.getCliente().pesquisaEntregas();
        }

        return null;
    }

    public List<Entrega> getEntregas() {
        return entregas;
    }
}
