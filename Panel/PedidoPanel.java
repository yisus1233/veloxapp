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

        GridBagConstraints gbcRoot = new GridBagConstraints();

        // CARD central
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 64, 143), 1, true),
                BorderFactory.createEmptyBorder(28, 34, 28, 34))
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1
        gbc.gridy = 0; gbc.gridx = 0;
        card.add(new JLabel("ID Pedido:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(14); txtId.setEditable(false);
        card.add(txtId, gbc);

        gbc.gridx = 2;
        card.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 3;
        txtFecha = new JTextField(12); txtFecha.setEditable(false);
        card.add(txtFecha, gbc);

        // Fila 2
        gbc.gridy++; gbc.gridx = 0;
        card.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        comboClientes = new JComboBox<>();
        card.add(comboClientes, gbc);

        gbc.gridx = 2;
        card.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 3;
        comboEstado = new JComboBox<>(new String[]{"Recepcionado", "En ruta"});
        card.add(comboEstado, gbc);

        // Fila 3
        gbc.gridy++; gbc.gridx = 0;
        card.add(new JLabel("Total:"), gbc);
        gbc.gridx = 1;
        txtTotal = new JTextField(10); txtTotal.setEditable(false);
        card.add(txtTotal, gbc);

        // Fila 4: botones
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER;
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
        JLabel lblTitulo = new JLabel("Registro de Pedido");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(34, 64, 143));
        panelTitulo.add(lblTitulo);

        gbcRoot.gridx = 0; gbcRoot.gridy = 0; gbcRoot.anchor = GridBagConstraints.NORTH;
        gbcRoot.insets = new Insets(15, 0, 12, 0);
        add(panelTitulo, gbcRoot);

        gbcRoot.gridy = 1; gbcRoot.weighty = 1; gbcRoot.anchor = GridBagConstraints.CENTER;
        add(card, gbcRoot);

        // Lógica
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
