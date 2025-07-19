package veloxapp.form;

import veloxapp.conexion.conexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ProductoForm extends JFrame {
    private JTextField txtId, txtNombre, txtPrecio;
    private JComboBox<String> comboTamaño;
    private JButton btnRegistrar;

    public ProductoForm() {
        setTitle("Registro de Producto");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        txtId = new JTextField();
        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtPrecio.setEditable(false);

        comboTamaño = new JComboBox<>(new String[]{"Pequeño", "Mediano", "Grande"});
        comboTamaño.addActionListener(e -> calcularPrecioPorDistritoYTamaño());

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrarProducto());

        add(new JLabel("ID Producto:"));
        add(txtId);
        add(new JLabel("Nombre:"));
        add(txtNombre);
        add(new JLabel("Tamaño:"));
        add(comboTamaño);
        add(new JLabel("Precio:"));
        add(txtPrecio);
        add(btnRegistrar);

        generarNuevoId();
    }

    private void generarNuevoId() {
        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement("SELECT TOP 1 idproducto FROM Producto ORDER BY idproducto DESC");
             ResultSet rs = ps.executeQuery()) {
            String nuevoId = "P001";
            if (rs.next()) {
                String ultimoId = rs.getString("idproducto");
                int numero = Integer.parseInt(ultimoId.substring(1)) + 1;
                nuevoId = String.format("P%03d", numero);
            }
            txtId.setText(nuevoId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calcularPrecioPorDistritoYTamaño() {
        String tamaño = (String) comboTamaño.getSelectedItem();
        String distrito = obtenerDistritoUltimoCliente();

        if (tamaño == null || distrito == null || tamaño.isEmpty() || distrito.isEmpty()) {
            txtPrecio.setText("");
            return;
        }

        try {
            double valorTamaño = obtenerValorPorTamaño(tamaño);
            double valorDistrito = obtenerValorPorDistrito(distrito);
            double precio = valorTamaño * valorDistrito;

            txtPrecio.setText(String.format("%.2f", precio));
        } catch (Exception e) {
            txtPrecio.setText("0.00");
        }
    }

    private String obtenerDistritoUltimoCliente() {
        String distrito = "";
        try (Connection conn = conexionBD.conectar()) {
            String sql = "SELECT TOP 1 distrito FROM Cliente ORDER BY fecharegistro DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                distrito = rs.getString("distrito");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distrito;
    }

    private double obtenerValorPorTamaño(String tamaño) {
        switch (tamaño.toLowerCase()) {
            case "pequeño": return 1.0;
            case "mediano": return 1.5;
            case "grande": return 2.0;
            default: return 1.0;
        }
    }

    private double obtenerValorPorDistrito(String distrito) {
        switch (distrito.toLowerCase()) {
            case "miraflores": return 5.0;
            case "san isidro": return 6.0;
            case "villa el salvador": return 3.0;
            default: return 4.0;
        }
    }

    private void registrarProducto() {
        try (Connection conn = conexionBD.conectar()) {
            String sql = "INSERT INTO Producto (idproducto, nombre, tamaño, precio) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtId.getText());
            ps.setString(2, txtNombre.getText());
            ps.setString(3, (String) comboTamaño.getSelectedItem());
            ps.setDouble(4, Double.parseDouble(txtPrecio.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Producto registrado exitosamente.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar producto: " + e.getMessage());
        }
    }
}
