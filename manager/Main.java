package veloxapp;

import veloxapp.modelo.Cliente;
import veloxapp.manager.ClienteManager;


public class Main {
    public static void main(String[] args) {
        ClienteManager manager = new ClienteManager();

        String nuevoId = manager.generarNuevoIdCliente();
        Cliente cliente = new Cliente();
        cliente.setIdcliente(nuevoId);
        cliente.setNombre("Tienda Gamarra 1");
        cliente.setRuc("20112233445");
        cliente.setDireccion("Av. Gamarra 987");
        cliente.setCelular("912345678");
        cliente.setFecharegistro("2025-07-15");

        manager.insertarCliente(cliente);
    }
}
