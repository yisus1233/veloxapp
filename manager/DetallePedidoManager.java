package veloxapp.manager;

import veloxapp.conexion.conexionBD;
import veloxapp.modelo.DetallePedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DetallePedidoManager {

    public String generarNuevoIdDetalle() {
        String nuevoId = "DP001";
        String sql = "SELECT TOP 1 iddetalle FROM Detalle_Pedido ORDER BY iddetalle DESC";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String ultimoId = rs.getString("iddetalle");
                int numero = Integer.parseInt(ultimoId.substring(2)) + 1; // CORREGIDO
                nuevoId = String.format("DP%03d", numero);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error generando ID: " + e.getMessage());
        }

        return nuevoId;
    }

    public boolean insertarDetalle(DetallePedido detalle) {
        String sql = "INSERT INTO Detalle_Pedido (iddetalle, idpedido, idproducto, cantidad, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, detalle.getIddetalle());
            ps.setString(2, detalle.getIdpedido());
            ps.setString(3, detalle.getIdproducto());
            ps.setInt(4, detalle.getCantidad());
            ps.setDouble(5, detalle.getSubtotal());

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error insertando detalle: " + e.getMessage());
        }

        return false;
    }

    public List<String> obtenerIdsPedidos() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT idpedido FROM Pedido";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getString("idpedido"));
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo pedidos: " + e.getMessage());
        }

        return ids;
    }

    public List<String> obtenerIdsProductos() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT idproducto FROM Producto";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getString("idproducto"));
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo productos: " + e.getMessage());
        }

        return ids;
    }
}