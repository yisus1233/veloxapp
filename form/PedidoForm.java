package veloxapp.form;

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
        cargarClientes();

        comboEstado = new JComboBox<>(new String[]{"Recepcionado", "En ruta"});
        comboEstado.addActionListener(e -> calcularTotalPorEstado());

        txtFecha = new JTextField();
        txtFecha.setEditable(false);

        txtTotal = new JTextField();
        txtTotal.setEditable(false);

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
        calcularTotalPorEstado(); // Calcular al cargar el formulario

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
        // Invertir la lista antes de cargarla al combo
        Collections.reverse(lista);
        for (ClienteItem ci : lista) {
            comboClientes.addItem(ci);
        }
    }

    private void cargarFechaActual() {
        txtFecha.setText(LocalDate.now().toString());
    }

    // Este método calcula el total según el estado seleccionado
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

                new veloxapp.form.DetallePedidoForm().setVisible(true);
                dispose();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error al calcular el total.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        comboClientes.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
        calcularTotalPorEstado();
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
