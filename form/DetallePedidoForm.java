package veloxapp.form;

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

public class DetallePedidoForm extends JFrame {

    private final JTextField txtId, txtCantidad, txtSubtotal;
    private final JComboBox<String> comboPedido;
    private final JComboBox<ProductoItem> comboProducto;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar, btnSiguiente;

    public DetallePedidoForm() {
        setTitle("Registro de Detalle de Pedido");
        setSize(550, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(); txtId.setEditable(false);
        comboPedido = new JComboBox<>();
        comboProducto = new JComboBox<>();
        txtCantidad = new JTextField();
        txtSubtotal = new JTextField(); txtSubtotal.setEditable(false);

        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");
        btnSiguiente = new JButton("Siguiente");

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID Detalle:"), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("ID Pedido:"), gbc);
        gbc.gridx = 3;
        panel.add(comboPedido, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Producto:"), gbc);
        gbc.gridx = 1;
        panel.add(comboProducto, gbc);

        // Fila 3
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCantidad, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Subtotal:"), gbc);
        gbc.gridx = 3;
        panel.add(txtSubtotal, gbc);

        // Fila 4
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        btnPanel.add(btnRegistrar);
        btnPanel.add(btnLimpiar);
        btnPanel.add(btnCerrar);
        panel.add(btnPanel, gbc);

        // Fila 5
        gbc.gridy = 4;
        JPanel siguientePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        siguientePanel.add(btnSiguiente);
        panel.add(siguientePanel, gbc);

        add(panel);

        cargarDatos();
        generarNuevoId();

        btnRegistrar.addActionListener(e -> registrarDetalle());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
        });
        btnCerrar.addActionListener(e -> dispose());
        btnSiguiente.addActionListener(e -> {
            new veloxapp.form.EntregaForm().setVisible(true);
            dispose();
        });

        txtCantidad.addCaretListener(e -> calcularSubtotal());
        comboPedido.addActionListener(e -> calcularSubtotal());
    }

    private void cargarDatos() {
        DetallePedidoManager manager = new DetallePedidoManager();

        // Cargar pedidos en orden inverso
        comboPedido.removeAllItems();
        List<String> pedidos = manager.obtenerIdsPedidos();
        Collections.reverse(pedidos);
        for (String p : pedidos) comboPedido.addItem(p);

        // Cargar productos en orden inverso
        comboProducto.removeAllItems();
        List<ProductoInfo> productos = manager.obtenerProductosInfo();
        Collections.reverse(productos);
        for (ProductoInfo pr : productos) {
            comboProducto.addItem(new ProductoItem(pr.id, pr.nombre));
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

            ProductoItem productoSel = (ProductoItem) comboProducto.getSelectedItem();
            detalle.setIdproducto(productoSel != null ? productoSel.getId() : null);

            detalle.setCantidad(Integer.parseInt(txtCantidad.getText()));
            detalle.setSubtotal(Double.parseDouble(txtSubtotal.getText()));

            DetallePedidoManager manager = new DetallePedidoManager();
            if (manager.insertarDetalle(detalle)) {
                JOptionPane.showMessageDialog(this, "✅ Detalle registrado con éxito.");
                new veloxapp.form.EntregaForm().setVisible(true);
                dispose();
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

    public static class ProductoItem {
        private final String id;
        private final String nombre;

        public ProductoItem(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public String getId() { return id; }
        public String toString() { return nombre; }
    }

    public static class ProductoInfo {
        public final String id;
        public final String nombre;

        public ProductoInfo(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }
}
