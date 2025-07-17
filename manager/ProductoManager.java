package veloxapp.manager;

import veloxapp.conexion.conexionBD;
import veloxapp.modelo.Producto;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoManager {

    // Generar nuevo ID automático tipo P0001, P0002...
    public String generarNuevoIdProducto() {
        String nuevoId = "P0001";
        String sql = "SELECT TOP 1 idproducto FROM Producto ORDER BY idproducto DESC";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String ultimoId = rs.getString("idproducto");
                int numero = Integer.parseInt(ultimoId.substring(1)) + 1;
                nuevoId = String.format("P%04d", numero);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error generando ID de producto: " + e.getMessage());
        }

        return nuevoId;
    }

    // Verificar si el producto ya está registrado por nombre
    public boolean productoExiste(String nombre) {
        String sql = "SELECT COUNT(*) FROM Producto WHERE nombre = ?";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error verificando producto: " + e.getMessage());
        }

        return false;
    }

    // Insertar producto (si no existe)
    public boolean registrarProducto(Producto producto) {
        if (productoExiste(producto.getNombre())) {
            JOptionPane.showMessageDialog(null, "❌ Este producto ya está registrado.");
            return false;
        }

        String sql = "INSERT INTO Producto (idproducto, nombre, precio, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getIdproducto());
            ps.setString(2, producto.getNombre());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "✅ Producto registrado correctamente.");
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error insertando producto: " + e.getMessage());
        }

        return false;
    }
}
