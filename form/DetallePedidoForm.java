package veloxapp.form;

import veloxapp.manager.DetallePedidoManager;
import veloxapp.modelo.DetallePedido;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DetallePedidoForm extends JFrame {

    private final JTextField txtId, txtCantidad, txtSubtotal;
    private final JComboBox<String> comboPedido, comboProducto;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar;

    public DetallePedidoForm() {
        setTitle("Registro de Detalle de Pedido");
        setSize(500, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField();
        txtId.setEditable(false);

        comboPedido = new JComboBox<>();
        comboProducto = new JComboBox<>();
        txtCantidad = new JTextField();
        txtSubtotal = new JTextField();

        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");

        // Fila 1: ID Detalle y ID Pedido
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID Detalle:"), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("ID Pedido:"), gbc);
        gbc.gridx = 3;
        panel.add(comboPedido, gbc);

        // Fila 2: ID Producto
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("ID Producto:"), gbc);
        gbc.gridx = 1;
        panel.add(comboProducto, gbc);

        // Fila 3: Cantidad y Subtotal
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCantidad, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Subtotal:"), gbc);
        gbc.gridx = 3;
        panel.add(txtSubtotal, gbc);

        // Fila 4: Botones
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        btnPanel.add(btnRegistrar);
        btnPanel.add(btnLimpiar);
        btnPanel.add(btnCerrar);
        panel.add(btnPanel, gbc);

        add(panel);

        cargarDatos();
        generarNuevoId();

        btnRegistrar.addActionListener(e -> registrarDetalle());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
        });
        btnCerrar.addActionListener(e -> dispose());
    }

    private void cargarDatos() {
        DetallePedidoManager manager = new DetallePedidoManager();
        for (String p : manager.obtenerIdsPedidos()) comboPedido.addItem(p);
        for (String pr : manager.obtenerIdsProductos()) comboProducto.addItem(pr);
    }

    private void generarNuevoId() {
        DetallePedidoManager manager = new DetallePedidoManager();
        txtId.setText(manager.generarNuevoIdDetalle());
    }

    private void registrarDetalle() {
        try {
            DetallePedido detalle = new DetallePedido();
            detalle.setIddetalle(txtId.getText());
            detalle.setIdpedido((String) comboPedido.getSelectedItem());
            detalle.setIdproducto((String) comboProducto.getSelectedItem());
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

    private void limpiarCampos() {
        txtCantidad.setText("");
        txtSubtotal.setText("");
    }
}