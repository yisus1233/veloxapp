package veloxapp.manager;

import veloxapp.conexion.conexionBD;
import veloxapp.conexion.conexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoManager {

    public String generarNuevoId() {
        String nuevoId = "P0001";
        try {
            Connection conn = conexionBD.conectar();
            String sql = "SELECT MAX(idproducto) AS ultimo_id FROM producto";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getString("ultimo_id") != null) {
                String ultimoId = rs.getString("ultimo_id"); // Ej: "P0015"
                int numero = Integer.parseInt(ultimoId.substring(1)); // Extrae "0015" → 15
                numero++; // 16
                nuevoId = String.format("P%04d", numero); // "P0016"
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nuevoId;
    }
}
