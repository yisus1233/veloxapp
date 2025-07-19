package veloxapp.formulas;

import veloxapp.conexion.conexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CalculadoraPrecio {

    public static double obtenerPrecio(String idcliente, String idproducto) {
        double precio = 0.0;

        try {
            Connection conn = conexionBD.conectar();

            String sql = """
                SELECT p.precio
                FROM Producto p
                JOIN Detalle_Pedido dp ON p.idproducto = dp.idproducto
                JOIN Pedido pe ON dp.idpedido = pe.idpedido
                JOIN Cliente c ON pe.idcliente = c.idcliente
                WHERE c.idcliente = ? AND p.idproducto = ?
                """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idcliente);
            ps.setString(2, idproducto);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                precio = rs.getDouble("precio");
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return precio;
    }
}
