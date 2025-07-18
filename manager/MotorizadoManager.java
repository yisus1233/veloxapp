package veloxapp.manager;

import veloxapp.conexion.conexionBD;
import veloxapp.modelo.Motorizado;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MotorizadoManager {

    // Generar nuevo ID automático tipo M0001, M0002...
    public String generarNuevoIdMotorizado() {
        String nuevoId = "M0001";
        String sql = "SELECT TOP 1 idmotorizado FROM Motorizado ORDER BY idmotorizado DESC";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String ultimoId = rs.getString("idmotorizado");
                int numero = Integer.parseInt(ultimoId.substring(1)) + 1;
                nuevoId = String.format("M%04d", numero);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error generando ID: " + e.getMessage());
        }

        return nuevoId;
    }

    // Insertar motorizado
    public boolean insertarMotorizado(Motorizado motorizado) {
        String sql = "INSERT INTO Motorizado (idmotorizado, nombre, celular, placa) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, motorizado.getIdmotorizado());
            ps.setString(2, motorizado.getNombre());
            ps.setString(3, motorizado.getCelular());
            ps.setString(4, motorizado.getPlaca());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error insertando motorizado: " + e.getMessage());
        }

        return false;
    }
}