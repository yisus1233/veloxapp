package veloxapp.manager;

import veloxapp.conexion.conexionBD;
import veloxapp.conexion.conexionBD;
import veloxapp.modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

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
            JOptionPane.showMessageDialog(null, "Error generando ID: " + e.getMessage());
        }

        return nuevoId;
    }

    // Verificar si el cliente ya está registrado por RUC
    public boolean clienteExiste(String ruc) {
        String sql = "SELECT COUNT(*) FROM Cliente WHERE ruc = ?";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ruc);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error verificando cliente: " + e.getMessage());
        }

        return false;
    }

    // Insertar cliente (si no existe)
    public boolean insertarCliente(Cliente cliente) {
        if (clienteExiste(cliente.getRuc())) {
            JOptionPane.showMessageDialog(null, "❌ Este cliente ya está registrado.");
            return false;
        }

        String sql = "INSERT INTO Cliente (idcliente, nombre, ruc, direccion, celular, fecharegistro) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getIdcliente());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getRuc());
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getCelular());
            ps.setString(6, cliente.getFecharegistro());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "✅ Cliente registrado correctamente.");
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error insertando cliente: " + e.getMessage());
        }

        return false;
    }
}
