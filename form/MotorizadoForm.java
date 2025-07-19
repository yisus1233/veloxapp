package veloxapp.form;

import veloxapp.modelo.Motorizado;
import veloxapp.manager.MotorizadoManager;

import javax.swing.*;
import java.awt.*;

public class MotorizadoForm extends JFrame {

    private final JTextField txtId, txtCelular, txtPlaca;
    private final JComboBox<String> comboNombre;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar, btnSiguiente;

    private boolean motorizadoRegistrado = false;

    private final String[] nombresMotorizados = {
            "DANI", "DANIELA", "ISMAEL", "JOSE", "LUIS", "ALEX",
            "HENRRY", "DIEGO", "AMIEL", "PABLO", "ALEXANDER", " "
    };

    public MotorizadoForm() {
        setTitle("Registro de Motorizado");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        txtId = new JTextField(15);
        txtId.setEditable(false);

        comboNombre = new JComboBox<>(nombresMotorizados);
        txtCelular = new JTextField(15);
        txtPlaca = new JTextField(15);

        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");
        btnSiguiente = new JButton("Siguiente");

        int y = 0;

        // ID
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("ID Motorizado:"), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);
        y++;

        // Nombre
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panel.add(comboNombre, gbc);
        y++;

        // Celular
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Celular:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCelular, gbc);
        y++;

        // Placa
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPlaca, gbc);
        y++;

        // Botones Registrar y Limpiar
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(btnRegistrar, gbc);
        gbc.gridx = 1;
        panel.add(btnLimpiar, gbc);
        y++;

        // Botones Cerrar y Siguiente
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(btnSiguiente);
        bottomPanel.add(btnCerrar);
        panel.add(bottomPanel, gbc);

        add(panel);

        generarNuevoId();

        // Acciones
        btnRegistrar.addActionListener(e -> registrarMotorizado());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
        });
        btnCerrar.addActionListener(e -> dispose());

        btnSiguiente.addActionListener(e -> {
            if (motorizadoRegistrado) {
                new veloxapp.form.ProductoForm().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Primero registre el motorizado antes de continuar.");
            }
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