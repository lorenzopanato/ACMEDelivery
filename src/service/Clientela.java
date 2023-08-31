package service;

import model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class Clientela {

    List<Cliente> clientela;

    public Clientela() {
        clientela = new ArrayList<>();
    }

    public boolean cadastraCliente(Cliente cliente) {
        if(pesquisaCliente(cliente.getEmail()) != null)
            return false;

        return clientela.add(cliente);
    }

    public Cliente pesquisaCliente(String email) {
        for (Cliente c : clientela) {
            if(c.getEmail().equals(email))
                return c;
        }
        return null;
    }

    public List<Cliente> getClientela() {
        return clientela;
    }
}
