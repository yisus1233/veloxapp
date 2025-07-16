package veloxapp.form;

import veloxapp.modelo.Cliente;
import veloxapp.manager.ClienteManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ClienteForm extends JFrame {

    private final JTextField txtId, txtNombre, txtRuc, txtDireccion, txtCelular, txtFecha;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar;

    public ClienteForm() {
        setTitle("Registro de Cliente");
        setSize(420, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Crear campos
        txtId = new JTextField();
        txtId.setEditable(false);

        txtNombre = new JTextField();
        txtRuc = new JTextField();
        txtDireccion = new JTextField();
        txtCelular = new JTextField();

        txtFecha = new JTextField();
        txtFecha.setEditable(false);

        // Botones
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");

        // Añadir componentes
        panel.add(new JLabel("ID Cliente:"));
        panel.add(txtId);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("RUC:"));
        panel.add(txtRuc);
        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Celular:"));
        panel.add(txtCelular);
        panel.add(new JLabel("Fecha de Registro:"));
        panel.add(txtFecha);
        panel.add(btnRegistrar);
        panel.add(btnLimpiar);
        panel.add(btnCerrar);

        add(panel);

        // Generar valores automáticos
        generarNuevoId();
        cargarFechaActual();

        // Acciones
        btnRegistrar.addActionListener(e -> registrarCliente());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
            cargarFechaActual();
        });
        btnCerrar.addActionListener(e -> dispose());
    }

    private void generarNuevoId() {
        ClienteManager manager = new ClienteManager();
        String nuevoId = manager.generarNuevoIdCliente();
        txtId.setText(nuevoId);
    }

    private void cargarFechaActual() {
        txtFecha.setText(LocalDate.now().toString());
    }

    private void registrarCliente() {
        Cliente cliente = new Cliente();
        cliente.setIdcliente(txtId.getText());
        cliente.setNombre(txtNombre.getText());
        cliente.setRuc(txtRuc.getText());
        cliente.setDireccion(txtDireccion.getText());
        cliente.setCelular(txtCelular.getText());
        cliente.setFecharegistro(txtFecha.getText());

        ClienteManager manager = new ClienteManager();
        boolean exito = manager.insertarCliente(cliente);

        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Cliente registrado con éxito.");
            limpiarCampos();
            generarNuevoId();
            cargarFechaActual();
        } else {
            JOptionPane.showMessageDialog(this, "⚠️ El cliente ya está registrado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtRuc.setText("");
        txtDireccion.setText("");
        txtCelular.setText("");
    }
}
