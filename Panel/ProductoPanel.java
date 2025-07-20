package Panel;

import veloxapp.conexion.conexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ProductoPanel extends JPanel {

    private final JTextField txtId, txtNombre, txtPrecio;
    private final JComboBox<String> comboTamaño;
    private final JButton btnRegistrar, btnLimpiar;
    private boolean productoRegistrado = false;

    public ProductoPanel() {
        setBackground(new Color(247, 249, 255));
        setLayout(new GridBagLayout());

        GridBagConstraints gbcRoot = new GridBagConstraints();

        // CARD central
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 64, 143), 1, true),
                BorderFactory.createEmptyBorder(26, 32, 26, 32))
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID Producto
        gbc.gridy = 0; gbc.gridx = 0;
        card.add(new JLabel("ID Producto:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(16); txtId.setEditable(false);
        card.add(txtId, gbc);

        // Detalle Producto
        gbc.gridy++; gbc.gridx = 0;
        card.add(new JLabel("Detalle Producto:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(16);
        card.add(txtNombre, gbc);

        // Tamaño
        gbc.gridy++; gbc.gridx = 0;
        card.add(new JLabel("Tamaño:"), gbc);
        gbc.gridx = 1;
        comboTamaño = new JComboBox<>(new String[]{"Pequeño", "Mediano", "Grande"});
        card.add(comboTamaño, gbc);

        // Precio
        gbc.gridy++; gbc.gridx = 0;
        card.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        txtPrecio = new JTextField(16); txtPrecio.setEditable(false);
        card.add(txtPrecio, gbc);

        // Botones
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        panelBotones.setOpaque(false);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        card.add(panelBotones, gbc);

        // Título arriba del card
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setOpaque(false);
        JLabel lblTitulo = new JLabel("Registro de Producto");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(34, 64, 143));
        panelTitulo.add(lblTitulo);

        gbcRoot.gridx = 0; gbcRoot.gridy = 0; gbcRoot.anchor = GridBagConstraints.NORTH;
        gbcRoot.insets = new Insets(15, 0, 12, 0);
        add(panelTitulo, gbcRoot);

        gbcRoot.gridy = 1; gbcRoot.weighty = 1; gbcRoot.anchor = GridBagConstraints.CENTER;
        add(card, gbcRoot);

        generarNuevoId();

        comboTamaño.addActionListener(e -> calcularPrecioPorDistritoYTamaño());
        btnRegistrar.addActionListener(e -> registrarProducto());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
        });
    }

    private void generarNuevoId() {
        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement("SELECT TOP 1 idproducto FROM Producto ORDER BY idproducto DESC");
             ResultSet rs = ps.executeQuery()) {

            String nuevoId;
            if (rs.next()) {
                String ultimoId = rs.getString("idproducto"); // Por ejemplo "Po007"
                int numero = Integer.parseInt(ultimoId.replaceAll("\\D", "")) + 1; // Extrae solo números
                nuevoId = String.format("Po%03d", numero);
            } else {
                nuevoId = "Po001";
            }
            txtId.setText(nuevoId);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Error al generar ID del producto.");
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
            case "mediano": return 1.2;
            case "grande": return 1.5;
            default: return 1.0;
        }
    }

    private double obtenerValorPorDistrito(String distrito) {
        switch (distrito.toLowerCase()) {
            case "agustino": return 8.0;
            case "ate": return 8.0;
            case "breña": return 8.0;
            case "callao": return 8.0;
            case "chorrillos": return 8.0;
            case "comas": return 8.0;
            case "independencia": return 8.0;
            case "jesus maria": return 8.0;
            case "lima": return 8.0;
            case "magdalena": return 8.0;
            case "miraflores": return 8.0;
            case "rimac": return 8.0;
            case "san isidro": return 8.0;
            case "san miguel": return 8.0;
            case "sjl regular": return 8.0;
            case "la molina": return 8.0;
            case "surco": return 8.0;
            case "surquillo": return 8.0;
            case "carabayllo": return 12.0;
            case "ventanilla": return 15.0;
            case "manchay": return 18.0;
            default: return 8.0;
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
            JOptionPane.showMessageDialog(this, "\u2705 Producto registrado exitosamente.");
            productoRegistrado = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "\u274c Error al registrar producto: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        comboTamaño.setSelectedIndex(0);
        txtPrecio.setText("");
    }
}
