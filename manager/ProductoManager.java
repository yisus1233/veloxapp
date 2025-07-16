package veloxapp.manager;

import veloxapp.conexion.conexionBD;
import java.sql.*;

public class ProductoManager {

    public String generarNuevoIdProducto() {
        String nuevoId = "";
        Connection conn = conexionBD.obtenerConexion(); // 👈 Asegúrate de tener esto
        try {
            String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(idproducto, 2, LEN(idproducto)) AS INT)), 0) + 1 AS nuevo_id FROM producto";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idNumerico = rs.getInt("nuevo_id");
                nuevoId = String.format("P%04d", idNumerico);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nuevoId;
    }
}
