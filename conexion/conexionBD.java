package veloxapp.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexionBD {
    private static final String URL = "jdbc:sqlserver://veloxserver.database.windows.net:1433;"
            + "database=VELOXAPP;"
            + "user=admin_velox@veloxserver;"//holis
            + "password=POOgrupo14;"//hola mundo
            + "encrypt=true;"
            + "trustServerCertificate=false;"
            + "hostNameInCertificate=*.database.windows.net;"
            + "loginTimeout=30;";

    public static Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            System.out.println("✅ Conexión exitosa a Azure SQL");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        conectar();
    }

    public static Connection obtenerConexion() {
        return null;
    }
}
