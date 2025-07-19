package veloxapp.form;

import veloxapp.conexion.conexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ProductoForm extends JFrame {

    private JTextField txtId, txtNombre, txtPrecio;
    private JComboBox<String> comboTamaño;
    private JButton btnRegistrar, btnSiguiente, btnCerrar;

    private boolean productoRegistrado = false;

    public ProductoForm() {
        setTitle("Registro de Producto");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(20);
        txtId.setEditable(false);
        txtNombre = new JTextField(20);
        txtPrecio = new JTextField(20);
        txtPrecio.setEditable(false);

        comboTamaño = new JComboBox<>(new String[]{"Pequeño", "Mediano", "Grande"});
        comboTamaño.addActionListener(e -> calcularPrecioPorDistritoYTamaño());

        btnRegistrar = new JButton("Registrar");
        btnSiguiente = new JButton("Siguiente");
        btnCerrar = new JButton("Cerrar");

        // Fila 1 - ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID Producto:"), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);

        // Fila 2 - Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        // Fila 3 - Tamaño
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Tamaño:"), gbc);
        gbc.gridx = 1;
        panel.add(comboTamaño, gbc);

        // Fila 4 - Precio
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPrecio, gbc);

        // Fila 5 - Botones Registrar y Siguiente
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(btnRegistrar, gbc);
        gbc.gridx = 1;
        panel.add(btnSiguiente, gbc);

        // Fila 6 - Cerrar
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnCerrar, gbc);

        add(panel);

        generarNuevoId();

        // Eventos
        btnRegistrar.addActionListener(e -> registrarProducto());
        btnSiguiente.addActionListener(e -> {
            if (productoRegistrado) {
                new veloxapp.form.PedidoForm().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Primero registre el producto antes de continuar.");
            }
        });
        btnCerrar.addActionListener(e -> dispose());
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
            JOptionPane.showMessageDialog(this, "✅ Producto registrado exitosamente.");
            productoRegistrado = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al registrar producto: " + e.getMessage());
        }
    }
}