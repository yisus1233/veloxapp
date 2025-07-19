package veloxapp.manager;

import veloxapp.conexion.conexionBD;
import veloxapp.modelo.Entrega;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class EntregaManager {

    public String generarNuevoIdEntrega() {
        String nuevoId = "EN001";
        String sql = "SELECT TOP 1 identrega FROM Entrega ORDER BY identrega DESC";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String ultimoId = rs.getString("identrega"); // por ejemplo: "EN007"
                int numero = Integer.parseInt(ultimoId.substring(2)); // extrae "007"
                numero++;
                nuevoId = String.format("EN%03d", numero); // genera "EN008"
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error generando ID: " + e.getMessage());
        }

        return nuevoId;
    }

    public boolean insertarEntrega(Entrega entrega) {
        String sql = "INSERT INTO Entrega (identrega, idpedido, idmotorizado, fechaentrega, horaentrega, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entrega.getIdentrega());
            ps.setString(2, entrega.getIdpedido());
            ps.setString(3, entrega.getIdmotorizado());
            ps.setDate(4, entrega.getFechaentrega());
            ps.setTime(5, entrega.getHoraentrega());
            ps.setString(6, entrega.getEstado());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error insertando entrega: " + e.getMessage());
        }

        return false;
    }

    public List<String> obtenerIdsPedidos() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT idpedido FROM Pedido";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) ids.add(rs.getString("idpedido"));

        } catch (Exception e) {
            System.out.println("Error obteniendo pedidos: " + e.getMessage());
        }

        return ids;
    }

    public List<String> obtenerIdsMotorizados() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT idmotorizado FROM Motorizado";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) ids.add(rs.getString("idmotorizado"));

        } catch (Exception e) {
            System.out.println("Error obteniendo motorizados: " + e.getMessage());
        }

        return ids;
    }
}