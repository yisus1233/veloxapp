package Panel;

import veloxapp.manager.EntregaManager;
import veloxapp.modelo.Entrega;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class EntregaPanel extends JPanel {

    private final JTextField txtId;
    private final JComboBox<String> comboPedido, comboMotorizado, comboEstado;
    private final JButton btnRegistrar, btnLimpiar, btnInicio;

    public EntregaPanel() {
        setBackground(new Color(247, 249, 255));
        setLayout(new GridBagLayout());

        GridBagConstraints gbcRoot = new GridBagConstraints();

        // CARD central
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 64, 143), 1, true),
                BorderFactory.createEmptyBorder(24, 32, 24, 32))
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1
        gbc.gridy = 0; gbc.gridx = 0;
        card.add(new JLabel("ID Entrega:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(12); txtId.setEditable(false);
        card.add(txtId, gbc);

        gbc.gridx = 2;
        card.add(new JLabel("ID Pedido:"), gbc);
        gbc.gridx = 3;
        comboPedido = new JComboBox<>();
        card.add(comboPedido, gbc);

        // Fila 2
        gbc.gridy++; gbc.gridx = 0;
        card.add(new JLabel("ID Motorizado:"), gbc);
        gbc.gridx = 1;
        comboMotorizado = new JComboBox<>();
        card.add(comboMotorizado, gbc);

        gbc.gridx = 2;
        card.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 3;
        comboEstado = new JComboBox<>(new String[]{"Entregado", "No entregado", "Cancelado"});
        card.add(comboEstado, gbc);

        // Fila 3: botones
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnInicio = new JButton("Volver al Inicio");
        panelBotones.setOpaque(false);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnInicio);
        card.add(panelBotones, gbc);

        // Título arriba del card
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setOpaque(false);
        JLabel lblTitulo = new JLabel("Registro de Entrega");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(34, 64, 143));
        panelTitulo.add(lblTitulo);

        gbcRoot.gridx = 0; gbcRoot.gridy = 0; gbcRoot.anchor = GridBagConstraints.NORTH;
        gbcRoot.insets = new Insets(15, 0, 12, 0);
        add(panelTitulo, gbcRoot);

        gbcRoot.gridy = 1; gbcRoot.weighty = 1; gbcRoot.anchor = GridBagConstraints.CENTER;
        add(card, gbcRoot);

        // Lógica
        cargarCombos();
        generarNuevoId();

        btnRegistrar.addActionListener(e -> registrarEntrega());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
        });

        btnInicio.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "🔁 Iniciando nuevo flujo desde Cliente.");
            // Aquí puedes mandar a tu panel de cliente si tienes referencia al contenedor principal
        });
    }

    private void cargarCombos() {
        EntregaManager manager = new EntregaManager();
        List<String> pedidos = manager.obtenerIdsPedidos();
        Collections.reverse(pedidos);
        comboPedido.removeAllItems();
        for (String p : pedidos) comboPedido.addItem(p);

        List<String> motorizados = manager.obtenerIdsMotorizados();
        Collections.reverse(motorizados);
        comboMotorizado.removeAllItems();
        for (String m : motorizados) comboMotorizado.addItem(m);
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
                JOptionPane.showMessageDialog(this, "✅ Entrega registrada con éxito. Fin del proceso.");
                limpiarCampos();
                generarNuevoId();
                cargarCombos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al registrar entrega.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Datos inválidos. Revise los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        if (comboPedido.getItemCount() > 0) comboPedido.setSelectedIndex(0);
        if (comboMotorizado.getItemCount() > 0) comboMotorizado.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
    }
}
