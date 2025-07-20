package veloxapp.form;

import veloxapp.modelo.Pedido;
import veloxapp.manager.PedidoManager;
import veloxapp.manager.ClienteManager;
import veloxapp.conexion.conexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class PedidoForm extends JFrame {

    private final JTextField txtId, txtFecha, txtTotal;
    private final JComboBox<ClienteItem> comboClientes;
    private final JComboBox<String> comboEstado;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar, btnSiguiente;

    private boolean pedidoRegistrado = false;

    public PedidoForm() {
        setTitle("Registro de Pedido");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtId = new JTextField();
        txtId.setEditable(false);

        comboClientes = new JComboBox<>();
        cargarClientes(); // Llenar combo con nombres

        comboEstado = new JComboBox<>(new String[]{"Pendiente", "Procesado", "Entregado"});

        txtFecha = new JTextField();
        txtFecha.setEditable(false);

        txtTotal = new JTextField();
        txtTotal.setEditable(false); // El total es automático

        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");
        btnSiguiente = new JButton("Siguiente");

        // Componentes
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

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnSiguiente);
        bottomPanel.add(btnCerrar);
        add(bottomPanel, BorderLayout.SOUTH);

        generarNuevoId();
        cargarFechaActual();
        calcularTotalPedido(); // Llama aquí o después de añadir detalles

        btnRegistrar.addActionListener(e -> registrarPedido());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnCerrar.addActionListener(e -> dispose());

        btnSiguiente.addActionListener(e -> {
            if (pedidoRegistrado) {
                new veloxapp.form.DetallePedidoForm().setVisible(true);
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

    // Llena el combo con objetos ClienteItem (nombre visible, id oculto)
    private void cargarClientes() {
        comboClientes.removeAllItems();
        try (Connection conn = conexionBD.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT idcliente, nombre FROM Cliente")) {
            while (rs.next()) {
                comboClientes.addItem(new ClienteItem(rs.getString("idcliente"), rs.getString("nombre")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarFechaActual() {
        txtFecha.setText(LocalDate.now().toString());
    }

    // Calcula el total sumando los subtotales de los detalles de pedido asociados al id de pedido generado
    private void calcularTotalPedido() {
        String idPedido = txtId.getText();
        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT SUM(subtotal) as total FROM Detalle_Pedido WHERE idpedido = ?")) {
            ps.setString(1, idPedido);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("total");
                txtTotal.setText(String.format("%.2f", total));
            } else {
                txtTotal.setText("0.00");
            }
        } catch (Exception e) {
            txtTotal.setText("0.00");
        }
    }

    private void registrarPedido() {
        try {
            Pedido pedido = new Pedido();
            pedido.setIdpedido(txtId.getText());

            ClienteItem clienteSel = (ClienteItem) comboClientes.getSelectedItem();
            pedido.setIdcliente(clienteSel != null ? clienteSel.getId() : null);

            pedido.setEstado((String) comboEstado.getSelectedItem());
            pedido.setFechapedido(txtFecha.getText());

            // El total se calcula automáticamente
            double total = Double.parseDouble(txtTotal.getText().isEmpty() ? "0" : txtTotal.getText());
            pedido.setTotal(total);

            PedidoManager manager = new PedidoManager();
            boolean exito = manager.registrarPedido(pedido);

            if (exito) {
                JOptionPane.showMessageDialog(this, "✅ Pedido registrado con éxito.");
                pedidoRegistrado = true;

                // Abrir DetallePedidoForm automáticamente
                new veloxapp.form.DetallePedidoForm().setVisible(true);
                dispose();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠ Ocurrió un error al calcular el total.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        comboClientes.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
        txtTotal.setText("");
    }

    // Clase interna para mostrar el nombre pero guardar el ID
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
