package veloxapp.form;

import veloxapp.modelo.Pedido;
import veloxapp.manager.PedidoManager;
import veloxapp.manager.ClienteManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PedidoForm extends JFrame {

    private final JTextField txtId, txtFecha, txtTotal;
    private final JComboBox<String> comboClientes;
    private final JComboBox<String> comboEstado;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar;
    private final JButton btnSiguiente;

    private boolean pedidoRegistrado = false;

    public PedidoForm() {
        setTitle("Registro de Pedido");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campos
        txtId = new JTextField();
        txtId.setEditable(false);

        comboClientes = new JComboBox<>();
        comboEstado = new JComboBox<>(new String[]{"Pendiente", "Procesado", "Entregado"});

        txtFecha = new JTextField();
        txtFecha.setEditable(false);

        txtTotal = new JTextField();

        // Botones
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");
        btnSiguiente = new JButton("Siguiente");

        // Agregar componentes al panel principal
        panel.add(new JLabel("ID Pedido:"));
        panel.add(txtId);
        panel.add(new JLabel("Cliente:"));
        panel.add(comboClientes);
        panel.add(new JLabel("Estado:"));
        panel.add(comboEstado);
        panel.add(new JLabel("Fecha:"));
        panel.add(txtFecha);
        panel.add(new JLabel("Total:"));
        panel.add(txtTotal);
        panel.add(btnRegistrar);
        panel.add(btnLimpiar);

        add(panel, BorderLayout.CENTER);

        // Panel inferior con botón siguiente y cerrar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnSiguiente);
        bottomPanel.add(btnCerrar);
        add(bottomPanel, BorderLayout.SOUTH);

        generarNuevoId();
        cargarClientes();
        cargarFechaActual();

        // Eventos
        btnRegistrar.addActionListener(e -> registrarPedido());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnCerrar.addActionListener(e -> dispose());

        btnSiguiente.addActionListener(e -> {
            if (pedidoRegistrado) {
                veloxapp.form.DetallePedidoForm detalleForm = new veloxapp.form.DetallePedidoForm();
                detalleForm.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Primero registre el pedido antes de continuar.");
            }
        });
    }

    private void generarNuevoId() {
        PedidoManager manager = new PedidoManager();
        txtId.setText(manager.generarNuevoIdPedido());
    }

    private void cargarClientes() {
        ClienteManager manager = new ClienteManager();
        List<String> clientes = manager.obtenerIdsClientes();

        for (String id : clientes) {
            comboClientes.addItem(id);
        }
    }

    private void cargarFechaActual() {
        txtFecha.setText(LocalDate.now().toString());
    }

    private void registrarPedido() {
        try {
            Pedido pedido = new Pedido();
            pedido.setIdpedido(txtId.getText());
            pedido.setIdcliente((String) comboClientes.getSelectedItem());
            pedido.setEstado((String) comboEstado.getSelectedItem());
            pedido.setFechapedido(txtFecha.getText());
            pedido.setTotal(Double.parseDouble(txtTotal.getText()));

            PedidoManager manager = new PedidoManager();
            boolean exito = manager.registrarPedido(pedido);

            if (exito) {
                JOptionPane.showMessageDialog(this, "✅ Pedido registrado con éxito.");
                pedidoRegistrado = true; // Permitir avanzar
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠ Ingrese un valor válido para el total.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        comboClientes.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
        txtTotal.setText("");
    }
}
