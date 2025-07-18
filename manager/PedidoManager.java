package veloxapp.manager;

import veloxapp.conexion.conexionBD;
import veloxapp.modelo.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PedidoManager {

    public boolean registrarPedido(Pedido pedido) {
        String sql = "INSERT INTO Pedido (idpedido, idcliente, fecha, estado, total) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pedido.getIdpedido());
            stmt.setString(2, pedido.getIdcliente());
            stmt.setString(3, pedido.getFechapedido());
            stmt.setString(4, pedido.getEstado());
            stmt.setDouble(5, pedido.getTotal());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.err.println("❌ Error al registrar pedido: " + e.getMessage());
            return false;
        }
    }

    public String generarNuevoIdPedido() {
        String nuevoId = "PED001";  // Default
        String sql = "SELECT TOP 1 idpedido FROM Pedido ORDER BY idpedido DESC";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String ultimoId = rs.getString("idpedido").replace("PED", "");
                int idNum = Integer.parseInt(ultimoId) + 1;
                nuevoId = String.format("PED%03d", idNum);
            }

        } catch (Exception e) {
            System.err.println("⚠ Error generando nuevo ID: " + e.getMessage());
        }

        return nuevoId;
    }
}