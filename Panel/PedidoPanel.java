package Panel;

import veloxapp.modelo.Pedido;
import veloxapp.manager.PedidoManager;
import veloxapp.conexion.conexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PedidoPanel extends JPanel {

    private final JTextField txtId, txtFecha, txtTotal;
    private final JComboBox<ClienteItem> comboClientes;
    private final JComboBox<String> comboEstado;
    private final JButton btnRegistrar, btnLimpiar;
    private boolean pedidoRegistrado = false;

    public PedidoPanel() {
        setBackground(new Color(247, 249, 255));
        setLayout(new GridBagLayout());

        // ---- CARD central moderno ----
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
        card.setPreferredSize(new Dimension(500, 320));
        card.setBorder(BorderFactory.createEmptyBorder(28, 38, 22, 38));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ---- TÍTULO y subtítulo ----
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JLabel lblTitulo = new JLabel("🧾 Registro de Pedido");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(34, 64, 143));
        card.add(lblTitulo, gbc);

        gbc.gridy++;
        JLabel lblSub = new JLabel("Agrega un nuevo pedido al sistema de forma fácil y rápida");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        lblSub.setForeground(new Color(80, 120, 180));
        card.add(lblSub, gbc);

        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(210, 210, 240));
        sep.setPreferredSize(new Dimension(370, 2));
        card.add(sep, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // ---- CAMPOS ----

        // Fila 1: ID y Fecha
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("ID Pedido:", "🔖"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(12); txtId.setEditable(false);
        card.add(txtId, gbc);

        gbc.gridx = 2;
        card.add(labelIcon("Fecha:", "📅"), gbc);
        gbc.gridx = 3;
        txtFecha = new JTextField(12); txtFecha.setEditable(false);
        card.add(txtFecha, gbc);

        // Fila 2: Cliente y Estado
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Cliente:", "👤"), gbc);
        gbc.gridx = 1;
        comboClientes = new JComboBox<>();
        card.add(comboClientes, gbc);

        gbc.gridx = 2;
        card.add(labelIcon("Estado:", "🚚"), gbc);
        gbc.gridx = 3;
        comboEstado = new JComboBox<>(new String[]{"Recepcionado", "En ruta"});
        card.add(comboEstado, gbc);

        // Fila 3: Total
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Total:", "💵"), gbc);
        gbc.gridx = 1;
        txtTotal = new JTextField(10); txtTotal.setEditable(false);
        card.add(txtTotal, gbc);

        // Fila 4: Botones
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        panelBotones.setOpaque(false);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        card.add(panelBotones, gbc);

        // ---- Centrado del card en el panel principal ----
        GridBagConstraints gbcRoot = new GridBagConstraints();
        gbcRoot.gridx = 0; gbcRoot.gridy = 0; gbcRoot.weightx = 1; gbcRoot.weighty = 1;
        gbcRoot.anchor = GridBagConstraints.CENTER;
        add(card, gbcRoot);

        // ---- Lógica de negocio ----
        generarNuevoId();
        cargarClientes();
        cargarFechaActual();
        calcularTotalPorEstado();

        // Listeners
        btnRegistrar.addActionListener(e -> registrarPedido());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
            cargarFechaActual();
        });
        comboEstado.addActionListener(e -> calcularTotalPorEstado());
    }

    // Label con emoji para consistencia visual
    private JLabel labelIcon(String texto, String emoji) {
        JLabel lbl = new JLabel(emoji + " " + texto);
        lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        lbl.setForeground(new Color(45, 70, 120));
        return lbl;
    }

    private void generarNuevoId() {
        PedidoManager manager = new PedidoManager();
        txtId.setText(manager.generarNuevoIdPedido());
    }

    private void cargarClientes() {
        comboClientes.removeAllItems();
        List<ClienteItem> lista = new ArrayList<>();
        try (Connection conn = conexionBD.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT idcliente, nombre FROM Cliente")) {
            while (rs.next()) {
                lista.add(new ClienteItem(rs.getString("idcliente"), rs.getString("nombre")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(lista);
        for (ClienteItem ci : lista) {
            comboClientes.addItem(ci);
        }
    }

    private void cargarFechaActual() {
        txtFecha.setText(LocalDate.now().toString());
    }

    private void calcularTotalPorEstado() {
        String estado = (String) comboEstado.getSelectedItem();
        if ("Recepcionado".equalsIgnoreCase(estado)) {
            txtTotal.setText("0.00");
        } else if ("En ruta".equalsIgnoreCase(estado)) {
            double precioUltimoProducto = obtenerPrecioUltimoProducto();
            txtTotal.setText(String.format("%.2f", precioUltimoProducto));
        }
    }

    private double obtenerPrecioUltimoProducto() {
        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement("SELECT TOP 1 precio FROM Producto ORDER BY idproducto DESC");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("precio");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.00;
    }

    private void registrarPedido() {
        try {
            Pedido pedido = new Pedido();
            pedido.setIdpedido(txtId.getText());
            ClienteItem clienteSel = (ClienteItem) comboClientes.getSelectedItem();
            pedido.setIdcliente(clienteSel != null ? clienteSel.getId() : null);
            pedido.setEstado((String) comboEstado.getSelectedItem());
            pedido.setFechapedido(txtFecha.getText());
            double total = Double.parseDouble(txtTotal.getText().isEmpty() ? "0" : txtTotal.getText());
            pedido.setTotal(total);

            PedidoManager manager = new PedidoManager();
            boolean exito = manager.registrarPedido(pedido);

            if (exito) {
                JOptionPane.showMessageDialog(this, "✅ Pedido registrado con éxito.");
                pedidoRegistrado = true;
                limpiarCampos();
                generarNuevoId();
                cargarFechaActual();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error al calcular el total.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        comboClientes.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
        txtTotal.setText("0.00");
    }

    // -------- Clase interna para combo Cliente --------
    public static class ClienteItem {
        private final String id;
        private final String nombre;

        public ClienteItem(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public String getId() {
            return id;
        }
        public String toString() {
            return nombre;
        }
    }
}
