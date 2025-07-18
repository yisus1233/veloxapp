package veloxapp.form;

import veloxapp.modelo.Cliente;
import veloxapp.manager.ClienteManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ClienteForm extends JFrame {

    private final JTextField txtId, txtNombre, txtDireccion, txtCelular, txtFecha;
    private final JComboBox<String> comboTienda, comboDistrito;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar;

    // Tiendas y distritos predefinidos
    private final String[] tiendas = {
            "AMATE", "ATLANTIS", "BEAUTY", "FITBELLA", "HND", "LIBERTY", "MEN LAD", "RIVAS", "THIFAS",
            "TUMI SOF", "WONDER", "FLOWER", "MARABELL", "VITELA", "ZASSY", "2BLEA", "TAWAQUI", "QUEEN",
            "CIOSNI", "TIMUY"
    };

    private final String[] distritos = {
            "AGUSTINO", "ATE", "BREÑA", "CALLAO", "CARABAYLLO", "CHORRILLOS", "COMAS", "INDEPENDENCIA",
            "JESUS MARIA", "LIMA", "MAGDALENA", "MANCHAY", "MIRAFLORES", "RIMAC", "SAN ISIDRO", "SAN MIGUEL",
            "SJL REGULAR", "LA MOLINA", "SURCO", "SURQUILLO", "VENTANILLA"
    };

    public ClienteForm() {
        setTitle("Registro de Cliente");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Campos
        txtId = new JTextField(15); txtId.setEditable(false);
        txtNombre = new JTextField(15);
        txtDireccion = new JTextField(15);
        txtCelular = new JTextField(15);
        txtFecha = new JTextField(15); txtFecha.setEditable(false);

        comboTienda = new JComboBox<>(tiendas);
        comboDistrito = new JComboBox<>(distritos);

        // Botones
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");

        int y = 0;

        // Fila 1 - ID y Nombre
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("ID Cliente:"), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3;
        panel.add(txtNombre, gbc);
        y++;

        // Fila 2 - Tienda y Distrito
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Tienda:"), gbc);
        gbc.gridx = 1;
        panel.add(comboTienda, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Distrito:"), gbc);
        gbc.gridx = 3;
        panel.add(comboDistrito, gbc);
        y++;

        // Fila 3 - Dirección y Celular
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDireccion, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Celular:"), gbc);
        gbc.gridx = 3;
        panel.add(txtCelular, gbc);
        y++;

        // Fila 4 - Fecha
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Fecha de Registro:"), gbc);
        gbc.gridx = 1;
        panel.add(txtFecha, gbc);
        y++;

        // Fila 5 - Botones
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(btnRegistrar, gbc);
        gbc.gridx = 1;
        panel.add(btnLimpiar, gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        panel.add(btnCerrar, gbc);

        add(panel);

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
        cliente.setTienda((String) comboTienda.getSelectedItem());
        cliente.setDistrito((String) comboDistrito.getSelectedItem());
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
        comboTienda.setSelectedIndex(0);
        comboDistrito.setSelectedIndex(0);
        txtDireccion.setText("");
        txtCelular.setText("");
    }
}