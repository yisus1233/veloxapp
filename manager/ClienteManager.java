package veloxapp.manager;

import veloxapp.conexion.conexionBD;
import veloxapp.modelo.Cliente;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteManager {

    // Generar nuevo ID automático tipo C0001, C0002...
    public String generarNuevoIdCliente() {
        String nuevoId = "C0001";
        String sql = "SELECT TOP 1 idcliente FROM Cliente ORDER BY idcliente DESC";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String ultimoId = rs.getString("idcliente");
                int numero = Integer.parseInt(ultimoId.substring(1)) + 1;
                nuevoId = String.format("C%04d", numero);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error generando ID: " + e.getMessage());
        }

        return nuevoId;
    }

    // Insertar cliente en la base de datos
    public boolean insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO Cliente (idcliente, nombre, tienda, distrito, direccion, celular, fecharegistro) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getIdcliente());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getTienda());
            ps.setString(4, cliente.getDistrito());
            ps.setString(5, cliente.getDireccion());
            ps.setString(6, cliente.getCelular());
            ps.setString(7, cliente.getFecharegistro());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "✅ Cliente registrado correctamente.");
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error insertando cliente: " + e.getMessage());
            return false;
        }
    }

    // Obtener todos los IDs de cliente (para comboBox en PedidoForm)
    public List<String> obtenerIdsClientes() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT idcliente FROM Cliente";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getString("idcliente"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener IDs de clientes: " + e.getMessage());
        }

        return ids;
    }
}