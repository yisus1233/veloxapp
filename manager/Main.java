package veloxapp.manager;

import veloxapp.modelo.Cliente;
import veloxapp.manager.ClienteManager;


public class Main {
    public static void main(String[] args) {
        ClienteManager manager = new ClienteManager();

        String nuevoId = manager.generarNuevoIdCliente();
        Cliente cliente = new Cliente();
        cliente.setIdcliente(nuevoId);
        cliente.setNombre("davluq");
        cliente.setTienda("Tienda123");       // CAMBIO: era setRuc()
        cliente.setDistrito("La Victoria");          // NUEVO
        cliente.setDireccion("Av. Gamarra 987");
        cliente.setCelular("912345678");
        cliente.setFecharegistro("2025-07-15");

        manager.insertarCliente(cliente);
    }
}
