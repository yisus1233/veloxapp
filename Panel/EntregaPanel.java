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

        // Card con fondo moderno y redondeado
        JPanel card = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 247));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(520, 280));
        card.setBorder(BorderFactory.createEmptyBorder(24, 34, 24, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título y subtítulo
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JLabel lblTitulo = new JLabel("🚚 Registro de Entrega");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(34, 64, 143));
        card.add(lblTitulo, gbc);

        gbc.gridy++;
        JLabel lblSub = new JLabel("Asigna motorizados y registra el estado de entrega fácilmente.");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        lblSub.setForeground(new Color(80, 120, 180));
        card.add(lblSub, gbc);

        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(210, 210, 240));
        sep.setPreferredSize(new Dimension(400, 2));
        card.add(sep, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 1: ID Entrega y Pedido
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("ID Entrega:", "🔖"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(11); txtId.setEditable(false);
        card.add(txtId, gbc);

        gbc.gridx = 2;
        card.add(labelIcon("ID Pedido:", "🧾"), gbc);
        gbc.gridx = 3;
        comboPedido = new JComboBox<>();
        card.add(comboPedido, gbc);

        // Fila 2: Motorizado y Estado
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Motorizado:", "🛵"), gbc);
        gbc.gridx = 1;
        comboMotorizado = new JComboBox<>();
        card.add(comboMotorizado, gbc);

        gbc.gridx = 2;
        card.add(labelIcon("Estado:", "📄"), gbc);
        gbc.gridx = 3;
        comboEstado = new JComboBox<>(new String[]{"Entregado", "No entregado", "Cancelado"});
        card.add(comboEstado, gbc);

        // Fila 3: botones
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 22, 0));
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnInicio = new JButton("Volver al Inicio");
        panelBotones.setOpaque(false);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnInicio);
        card.add(panelBotones, gbc);

        // Centrado general
        GridBagConstraints gbcRoot = new GridBagConstraints();
        gbcRoot.gridx = 0; gbcRoot.gridy = 0; gbcRoot.weightx = 1; gbcRoot.weighty = 1;
        gbcRoot.anchor = GridBagConstraints.CENTER;
        add(card, gbcRoot);

        // --- Lógica
        cargarCombos();
        generarNuevoId();

        btnRegistrar.addActionListener(e -> registrarEntrega());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
            cargarCombos();
        });

        btnInicio.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "🔁 Iniciando nuevo flujo desde Cliente.");
            // Aquí podrías lanzar el ClientePanel si tienes referencia
        });
    }

    private JLabel labelIcon(String texto, String emoji) {
        JLabel lbl = new JLabel(emoji + " " + texto);
        lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        lbl.setForeground(new Color(45, 70, 120));
        return lbl;
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
