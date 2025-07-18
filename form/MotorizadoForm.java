package veloxapp.form;

import veloxapp.modelo.Motorizado;
import veloxapp.manager.MotorizadoManager;


import javax.swing.*;
import java.awt.*;

public class MotorizadoForm extends JFrame {

    private final JTextField txtId, txtNombre, txtCelular, txtPlaca;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar;

    public MotorizadoForm() {
        setTitle("Registro de Motorizado");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campos de texto
        txtId = new JTextField();
        txtId.setEditable(false);

        txtNombre = new JTextField();
        txtCelular = new JTextField();
        txtPlaca = new JTextField();

        // Botones
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");

        // Añadir componentes
        panel.add(new JLabel("ID Motorizado:"));
        panel.add(txtId);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Celular:"));
        panel.add(txtCelular);
        panel.add(new JLabel("Placa:"));
        panel.add(txtPlaca);
        panel.add(btnRegistrar);
        panel.add(btnLimpiar);
        panel.add(new JLabel()); // espacio vacío
        panel.add(btnCerrar);

        add(panel);

        generarNuevoId();

        // Acciones
        btnRegistrar.addActionListener(e -> registrarMotorizado());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
        });
        btnCerrar.addActionListener(e -> dispose());
    }

    private void generarNuevoId() {
        MotorizadoManager manager = new MotorizadoManager();
        String nuevoId = manager.generarNuevoIdMotorizado();
        txtId.setText(nuevoId);
    }

    private void registrarMotorizado() {
        Motorizado motorizado = new Motorizado();
        motorizado.setIdmotorizado(txtId.getText());
        motorizado.setNombre(txtNombre.getText());
        motorizado.setCelular(txtCelular.getText());
        motorizado.setPlaca(txtPlaca.getText());

        MotorizadoManager manager = new MotorizadoManager();
        boolean exito = manager.insertarMotorizado(motorizado);

        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Motorizado registrado con éxito.");
            limpiarCampos();
            generarNuevoId();
        } else {
            JOptionPane.showMessageDialog(this, "⚠️ Error al registrar el motorizado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCelular.setText("");
        txtPlaca.setText("");
    }
}