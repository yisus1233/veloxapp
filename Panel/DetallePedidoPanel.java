package Panel;

import veloxapp.manager.DetallePedidoManager;
import veloxapp.modelo.DetallePedido;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import veloxapp.conexion.conexionBD;

public class DetallePedidoPanel extends JPanel {

    private final JTextField txtId, txtCantidad, txtSubtotal;
    private final JComboBox<String> comboPedido;
    private final JComboBox<DetallePedidoManager.ProductoInfo> comboProducto;
    private final JButton btnRegistrar, btnLimpiar, btnSiguiente;

    public DetallePedidoPanel() {
        setBackground(new Color(247, 249, 255));
        setLayout(new GridBagLayout());

        // Card central moderno
        JPanel card = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 246));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(570, 340));
        card.setBorder(BorderFactory.createEmptyBorder(26, 40, 26, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título y subtítulo
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JLabel lblTitulo = new JLabel("📦 Registro de Detalle de Pedido");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(34, 64, 143));
        card.add(lblTitulo, gbc);

        gbc.gridy++;
        JLabel lblSub = new JLabel("Asocia productos y cantidades a tu pedido rápidamente.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(80, 120, 180));
        card.add(lblSub, gbc);

        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(210, 210, 240));
        sep.setPreferredSize(new Dimension(420, 2));
        card.add(sep, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Campos — Fila 1: ID Detalle y Pedido
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("ID Detalle:", "🔖"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(10); txtId.setEditable(false);
        card.add(txtId, gbc);

        gbc.gridx = 2;
        card.add(labelIcon("ID Pedido:", "🧾"), gbc);
        gbc.gridx = 3;
        comboPedido = new JComboBox<>();
        card.add(comboPedido, gbc);

        // Fila 2: Producto
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Producto:", "📦"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        comboProducto = new JComboBox<>();
        card.add(comboProducto, gbc);
        gbc.gridwidth = 1;

        // Fila 3: Cantidad y Subtotal
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Cantidad:", "🔢"), gbc);
        gbc.gridx = 1;
        txtCantidad = new JTextField(8);
        card.add(txtCantidad, gbc);

        gbc.gridx = 2;
        card.add(labelIcon("Subtotal:", "💵"), gbc);
        gbc.gridx = 3;
        txtSubtotal = new JTextField(8); txtSubtotal.setEditable(false);
        card.add(txtSubtotal, gbc);

        // Fila 4: Botones
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnSiguiente = new JButton("Siguiente");
        panelBotones.setOpaque(false);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnSiguiente);
        card.add(panelBotones, gbc);

        // Centrado del card
        GridBagConstraints gbcRoot = new GridBagConstraints();
        gbcRoot.gridx = 0; gbcRoot.gridy = 0; gbcRoot.weightx = 1; gbcRoot.weighty = 1;
        gbcRoot.anchor = GridBagConstraints.CENTER;
        add(card, gbcRoot);

        // Lógica
        cargarDatos();
        generarNuevoId();

        btnRegistrar.addActionListener(e -> registrarDetalle());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
        });
        btnSiguiente.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Siguiente panel aquí (puedes llamar a EntregaPanel si lo tienes)");
        });

        txtCantidad.addCaretListener(e -> calcularSubtotal());
        comboPedido.addActionListener(e -> calcularSubtotal());
    }

    private JLabel labelIcon(String texto, String emoji) {
        JLabel lbl = new JLabel(emoji + " " + texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setForeground(new Color(45, 70, 120));
        return lbl;
    }

    private void cargarDatos() {
        DetallePedidoManager manager = new DetallePedidoManager();

        // Pedidos en orden inverso
        comboPedido.removeAllItems();
        List<String> pedidos = manager.obtenerIdsPedidos();
        Collections.reverse(pedidos);
        for (String p : pedidos) comboPedido.addItem(p);

        // Productos en orden inverso
        comboProducto.removeAllItems();
        List<DetallePedidoManager.ProductoInfo> productos = manager.obtenerProductosInfo();
        Collections.reverse(productos);
        for (DetallePedidoManager.ProductoInfo pr : productos) {
            comboProducto.addItem(pr);
        }
    }

    private void generarNuevoId() {
        DetallePedidoManager manager = new DetallePedidoManager();
        txtId.setText(manager.generarNuevoIdDetalle());
    }

    private void registrarDetalle() {
        try {
            if (txtCantidad.getText().trim().isEmpty() || txtSubtotal.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Todos los campos deben estar llenos.");
                return;
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setIddetalle(txtId.getText());
            detalle.setIdpedido((String) comboPedido.getSelectedItem());

            DetallePedidoManager.ProductoInfo productoSel = (DetallePedidoManager.ProductoInfo) comboProducto.getSelectedItem();
            detalle.setIdproducto(productoSel != null ? productoSel.id : null);

            detalle.setCantidad(Integer.parseInt(txtCantidad.getText()));
            detalle.setSubtotal(Double.parseDouble(txtSubtotal.getText()));

            DetallePedidoManager manager = new DetallePedidoManager();
            if (manager.insertarDetalle(detalle)) {
                JOptionPane.showMessageDialog(this, "✅ Detalle registrado con éxito.");
                limpiarCampos();
                generarNuevoId();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al registrar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Datos inválidos. Revise los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void calcularSubtotal() {
        try {
            String idpedido = (String) comboPedido.getSelectedItem();
            int cantidad = Integer.parseInt(txtCantidad.getText());

            if (idpedido == null || cantidad <= 0) {
                txtSubtotal.setText("");
                return;
            }

            double totalPedido = obtenerTotalPedido(idpedido);
            double subtotal = cantidad * totalPedido;
            txtSubtotal.setText(String.format("%.2f", subtotal));
        } catch (Exception e) {
            txtSubtotal.setText("0.00");
        }
    }

    private double obtenerTotalPedido(String idPedido) {
        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement("SELECT total FROM Pedido WHERE idpedido = ?")) {
            ps.setString(1, idPedido);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.00;
    }

    private void limpiarCampos() {
        txtCantidad.setText("");
        txtSubtotal.setText("");
    }
}
