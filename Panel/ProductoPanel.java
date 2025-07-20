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

        // --- CARD moderno ---
        JPanel card = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 242));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 34, 34);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(480, 340));
        card.setBorder(BorderFactory.createEmptyBorder(28, 38, 20, 38));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- TÍTULO y subtítulo ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("📦 Registro de Producto");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(22, 54, 120));
        card.add(lblTitulo, gbc);

        gbc.gridy++;
        JLabel lblSub = new JLabel("Agrega o edita los productos de tu tienda");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(80, 120, 180));
        card.add(lblSub, gbc);

        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(210, 210, 240));
        sep.setPreferredSize(new Dimension(360, 2));
        card.add(sep, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // ID Producto
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("ID Producto:", "🔖"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(16); txtId.setEditable(false);
        card.add(txtId, gbc);

        // Detalle Producto
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Detalle Producto:", "📋"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(16);
        card.add(txtNombre, gbc);

        // Tamaño
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Tamaño:", "📏"), gbc);
        gbc.gridx = 1;
        comboTamaño = new JComboBox<>(new String[]{"Pequeño", "Mediano", "Grande"});
        card.add(comboTamaño, gbc);

        // Precio
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Precio:", "💲"), gbc);
        gbc.gridx = 1;
        txtPrecio = new JTextField(16); txtPrecio.setEditable(false);
        card.add(txtPrecio, gbc);

        // Botones
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        panelBotones.setOpaque(false);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        card.add(panelBotones, gbc);

        // --- Centrar el card en el panel principal ---
        GridBagConstraints gbcRoot = new GridBagConstraints();
        gbcRoot.gridx = 0; gbcRoot.gridy = 0; gbcRoot.weightx = 1; gbcRoot.weighty = 1;
        gbcRoot.anchor = GridBagConstraints.CENTER;
        add(card, gbcRoot);

        generarNuevoId();

        comboTamaño.addActionListener(e -> calcularPrecioPorDistritoYTamaño());
        btnRegistrar.addActionListener(e -> registrarProducto());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
        });
    }

    // Label con emoji de icono
    private JLabel labelIcon(String texto, String emoji) {
        JLabel lbl = new JLabel(emoji + " " + texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setForeground(new Color(45, 70, 120));
        return lbl;
    }

    private void generarNuevoId() {
        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement("SELECT TOP 1 idproducto FROM Producto ORDER BY idproducto DESC");
             ResultSet rs = ps.executeQuery()) {

            String nuevoId;
            if (rs.next()) {
                String ultimoId = rs.getString("idproducto");
                int numero = Integer.parseInt(ultimoId.replaceAll("\\D", "")) + 1;
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
