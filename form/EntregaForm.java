package veloxapp.form;

import veloxapp.manager.EntregaManager;
import veloxapp.modelo.Entrega;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class EntregaForm extends JFrame {

    private final JTextField txtId;
    private final JComboBox<String> comboPedido, comboMotorizado, comboEstado;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar;

    public EntregaForm() {
        setTitle("Registro de Entrega");
        setSize(500, 250);
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
        comboMotorizado = new JComboBox<>();
        comboEstado = new JComboBox<>(new String[]{"Entregado", "No entregado", "Cancelado"});

        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID Entrega:"), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("ID Pedido:"), gbc);
        gbc.gridx = 3;
        panel.add(comboPedido, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("ID Motorizado:"), gbc);
        gbc.gridx = 1;
        panel.add(comboMotorizado, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 3;
        panel.add(comboEstado, gbc);

        // Fila 3 - Botones
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        btnPanel.add(btnRegistrar);
        btnPanel.add(btnLimpiar);
        btnPanel.add(btnCerrar);
        panel.add(btnPanel, gbc);

        add(panel);

        cargarCombos();
        generarNuevoId();

        btnRegistrar.addActionListener(e -> registrarEntrega());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
        });
        btnCerrar.addActionListener(e -> dispose());
    }

    private void cargarCombos() {
        EntregaManager manager = new EntregaManager();
        for (String p : manager.obtenerIdsPedidos()) comboPedido.addItem(p);
        for (String m : manager.obtenerIdsMotorizados()) comboMotorizado.addItem(m);
    }

    private void generarNuevoId() {
        EntregaManager manager = new EntregaManager();
        txtId.setText(manager.generarNuevoIdEntrega());
    }

    private void registrarEntrega() {
        try {
            Entrega entrega = new Entrega();
            entrega.setIdentrega(txtId.getText());
            entrega.setIdpedido((String) comboPedido.getSelectedItem());
            entrega.setIdmotorizado((String) comboMotorizado.getSelectedItem());
            entrega.setFechaentrega(Date.valueOf(LocalDate.now()));
            entrega.setHoraentrega(Time.valueOf(LocalTime.now()));
            entrega.setEstado((String) comboEstado.getSelectedItem());

            EntregaManager manager = new EntregaManager();
            if (manager.insertarEntrega(entrega)) {
                JOptionPane.showMessageDialog(this, "✅ Entrega registrada con éxito.");
                limpiarCampos();
                generarNuevoId();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al registrar entrega.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Datos inválidos. Revise los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        comboPedido.setSelectedIndex(0);
        comboMotorizado.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
    }
}
