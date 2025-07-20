package Panel;

import veloxapp.modelo.Motorizado;
import veloxapp.manager.MotorizadoManager;

import javax.swing.*;
import java.awt.*;

public class MotorizadoPanel extends JPanel {

    private final JTextField txtId, txtCelular, txtPlaca;
    private final JComboBox<String> comboNombre;
    private final JButton btnRegistrar, btnLimpiar;
    private boolean motorizadoRegistrado = false;

    private final String[] nombresMotorizados = {
            "DANI", "DANIELA", "ISMAEL", "JOSE", "LUIS", "ALEX",
            "HENRRY", "DIEGO", "AMIEL", "PABLO", "ALEXANDER"
    };

    public MotorizadoPanel() {
        setBackground(new Color(247, 249, 255));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // CARD central
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 64, 143), 1, true),
                BorderFactory.createEmptyBorder(26, 32, 26, 32))
        );

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0; gbc.gridx = 0;
        card.add(new JLabel("ID Motorizado:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(12); txtId.setEditable(false);
        card.add(txtId, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        card.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        comboNombre = new JComboBox<>(nombresMotorizados);
        card.add(comboNombre, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        card.add(new JLabel("Celular:"), gbc);
        gbc.gridx = 1;
        txtCelular = new JTextField(12);
        card.add(txtCelular, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        card.add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1;
        txtPlaca = new JTextField(12);
        card.add(txtPlaca, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
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
        JLabel lblTitulo = new JLabel("Registro de Motorizado");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(34, 64, 143));
        panelTitulo.add(lblTitulo);

        // Agregar título + card al centro
        GridBagConstraints rootGbc = new GridBagConstraints();
        rootGbc.gridx = 0; rootGbc.gridy = 0;
        rootGbc.anchor = GridBagConstraints.NORTH;
        rootGbc.insets = new Insets(15, 0, 12, 0);
        add(panelTitulo, rootGbc);

        rootGbc.gridy = 1;
        rootGbc.weighty = 1;
        rootGbc.anchor = GridBagConstraints.CENTER;
        add(card, rootGbc);

        generarNuevoId();

        // Acciones
        btnRegistrar.addActionListener(e -> registrarMotorizado());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
        });
    }

    private void generarNuevoId() {
        MotorizadoManager manager = new MotorizadoManager();
        String nuevoId = manager.generarNuevoIdMotorizado();
        txtId.setText(nuevoId);
    }

    private void registrarMotorizado() {
        Motorizado motorizado = new Motorizado();
        motorizado.setIdmotorizado(txtId.getText());
        motorizado.setNombre((String) comboNombre.getSelectedItem());
        motorizado.setCelular(txtCelular.getText());
        motorizado.setPlaca(txtPlaca.getText());

        MotorizadoManager manager = new MotorizadoManager();
        boolean exito = manager.insertarMotorizado(motorizado);

        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Motorizado registrado con éxito.");
            motorizadoRegistrado = true;
            limpiarCampos();
            generarNuevoId();
        } else {
            JOptionPane.showMessageDialog(this, "⚠️ Error al registrar el motorizado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        comboNombre.setSelectedIndex(0);
        txtCelular.setText("");
        txtPlaca.setText("");
    }
}
