package Panel;

import veloxapp.modelo.Motorizado;
import veloxapp.manager.MotorizadoManager;

import javax.swing.*;
import java.awt.*;

public class MotorizadoPanel extends JPanel {
    private final JTextField txtId, txtCelular, txtPlaca;
    private final JComboBox<String> comboNombre;
    private final JButton btnRegistrar, btnLimpiar;

    private final String[] nombresMotorizados = {
            "DANI", "DANIELA", "ISMAEL", "JOSE", "LUIS", "ALEX",
            "HENRRY", "DIEGO", "AMIEL", "PABLO", "ALEXANDER"
    };

    public MotorizadoPanel() {
        // --- Fondo tipo degradado suave ---
        setLayout(new GridBagLayout()) ;
        setBackground(new Color(239, 245, 255)); // Color suave azulito

        JPanel card = new JPanel(new GridBagLayout()) {
            // Para un borde redondeado tipo "tarjeta"
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 235));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 34, 34);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(480, 350));
        card.setBorder(BorderFactory.createEmptyBorder(24, 36, 24, 36));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Título grande con emoji (puedes usar tu icono PNG también)
        JLabel lblTitulo = new JLabel("🛵 Registro de Motorizado");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(19, 52, 110));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        card.add(lblTitulo, gbc);

        // Subtítulo decorativo
        gbc.gridy++;
        JLabel lblSub = new JLabel("Agrega aquí los datos del motorizado");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(80, 120, 180));
        card.add(lblSub, gbc);

        // Línea decorativa
        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(210, 210, 240));
        sep.setPreferredSize(new Dimension(360, 2));
        card.add(sep, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // --- Campos con iconos ---
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("ID Motorizado:", "🆔"), gbc);
        txtId = new JTextField(16); txtId.setEditable(false);
        gbc.gridx = 1; card.add(txtId, gbc);

        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Nombre:", "🧑‍💼"), gbc);
        comboNombre = new JComboBox<>(nombresMotorizados);
        gbc.gridx = 1; card.add(comboNombre, gbc);

        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Celular:", "📱"), gbc);
        txtCelular = new JTextField(16);
        gbc.gridx = 1; card.add(txtCelular, gbc);

        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Placa:", "🚙"), gbc);
        txtPlaca = new JTextField(16);
        gbc.gridx = 1; card.add(txtPlaca, gbc);

        // Botones centrados y con espacio arriba
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(20, 8, 8, 8);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        botones.setOpaque(false);
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        botones.add(btnRegistrar);
        botones.add(btnLimpiar);
        card.add(botones, gbc);

        // Añadir la tarjeta al centro del panel principal
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(card, c);

        // --- Lógica ejemplo: ID autogenerado (real con manager) ---
        MotorizadoManager manager = new MotorizadoManager();
        txtId.setText(manager.generarNuevoIdMotorizado());

        btnRegistrar.addActionListener(e -> {
            Motorizado m = new Motorizado();
            m.setIdmotorizado(txtId.getText());
            m.setNombre((String) comboNombre.getSelectedItem());
            m.setCelular(txtCelular.getText());
            m.setPlaca(txtPlaca.getText());

            boolean exito = manager.insertarMotorizado(m);
            if (exito) {
                JOptionPane.showMessageDialog(this, "✅ Motorizado registrado correctamente.");
                limpiarCampos();
                txtId.setText(manager.generarNuevoIdMotorizado());
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al registrar motorizado.");
            }
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    // Label con emoji de icono
    private JLabel labelIcon(String texto, String emoji) {
        JLabel lbl = new JLabel(emoji + " " + texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setForeground(new Color(45, 70, 120));
        return lbl;
    }

    private void limpiarCampos() {
        comboNombre.setSelectedIndex(0);
        txtCelular.setText("");
        txtPlaca.setText("");
    }
}
