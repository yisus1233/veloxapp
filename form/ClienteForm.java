package veloxapp.form;

import veloxapp.modelo.Cliente;
import veloxapp.manager.ClienteManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ClienteForm extends JFrame {

    private final JTextField txtId, txtNombre, txtDireccion, txtCelular, txtFecha;
    private final JComboBox<String> comboTienda;
    private final JComboBox<DistritoItem> comboDistrito;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar, btnSiguiente;

    private boolean clienteRegistrado = false;

    private final String[] tiendas = {
            "AMATE", "ATLANTIS", "BEAUTY", "FITBELLA", "HND", "LIBERTY", "MEN LAD", "RIVAS", "THIFAS",
            "TUMI SOF", "WONDER", "FLOWER", "MARABELL", "VITELA", "ZASSY", "2BLEA", "TAWAQUI", "QUEEN",
            "CIOSNI", "TIMUY"
    };

    private final DistritoItem[] distritos = {
            new DistritoItem("AGUSTINO", 8),
            new DistritoItem("ATE", 8),
            new DistritoItem("BREÑA", 8),
            new DistritoItem("CALLAO", 8),
            new DistritoItem("CARABAYLLO", 12),
            new DistritoItem("CHORRILLOS", 8),
            new DistritoItem("COMAS", 8),
            new DistritoItem("INDEPENDENCIA", 8),
            new DistritoItem("JESUS MARIA", 8),
            new DistritoItem("LIMA", 8),
            new DistritoItem("MAGDALENA", 8),
            new DistritoItem("MANCHAY", 18),
            new DistritoItem("MIRAFLORES", 8),
            new DistritoItem("RIMAC", 8),
            new DistritoItem("SAN ISIDRO", 8),
            new DistritoItem("SAN MIGUEL", 8),
            new DistritoItem("SJL REGULAR", 8),
            new DistritoItem("LA MOLINA", 8),
            new DistritoItem("SURCO", 8),
            new DistritoItem("SURQUILLO", 8),
            new DistritoItem("VENTANILLA", 15)
    };

    public ClienteForm() {
        setTitle("Registro de Cliente");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(15); txtId.setEditable(false);
        txtNombre = new JTextField(15);
        txtDireccion = new JTextField(15);
        txtCelular = new JTextField(15);
        txtFecha = new JTextField(15); txtFecha.setEditable(false);

        comboTienda = new JComboBox<>(tiendas);
        comboDistrito = new JComboBox<>(distritos);

        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");
        btnSiguiente = new JButton("Siguiente");

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("ID Cliente:"), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3;
        panel.add(txtNombre, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Tienda:"), gbc);
        gbc.gridx = 1;
        panel.add(comboTienda, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Distrito:"), gbc);
        gbc.gridx = 3;
        panel.add(comboDistrito, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDireccion, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Celular:"), gbc);
        gbc.gridx = 3;
        panel.add(txtCelular, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Fecha de Registro:"), gbc);
        gbc.gridx = 1;
        panel.add(txtFecha, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        panel.add(btnRegistrar, gbc);
        gbc.gridx = 1;
        panel.add(btnLimpiar, gbc);
        gbc.gridx = 2;
        panel.add(btnCerrar, gbc);
        gbc.gridx = 3;
        panel.add(btnSiguiente, gbc);

        add(panel);

        generarNuevoId();
        cargarFechaActual();

        btnRegistrar.addActionListener(e -> registrarCliente());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
            cargarFechaActual();
            clienteRegistrado = false;
        });
        btnCerrar.addActionListener(e -> dispose());

        // 🔁 ACTUALIZADO: secuencia al formulario MotorizadoForm
        btnSiguiente.addActionListener(e -> {
            if (!clienteRegistrado) {
                JOptionPane.showMessageDialog(this, "⚠️ Primero debes registrar al cliente.");
            } else {
                new veloxapp.form.MotorizadoForm().setVisible(true);
                dispose();
            }
        });
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

        DistritoItem distritoSeleccionado = (DistritoItem) comboDistrito.getSelectedItem();
        cliente.setDistrito(distritoSeleccionado.getNombre());

        cliente.setDireccion(txtDireccion.getText());
        cliente.setCelular(txtCelular.getText());
        cliente.setFecharegistro(txtFecha.getText());

        ClienteManager manager = new ClienteManager();
        boolean exito = manager.insertarCliente(cliente);

        if (exito) {
            clienteRegistrado = true;
            JOptionPane.showMessageDialog(this, "✅ Cliente registrado con éxito.");
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

    private static class DistritoItem {
        private final String nombre;
        private final int costoEnvio;

        public DistritoItem(String nombre, int costoEnvio) {
            this.nombre = nombre;
            this.costoEnvio = costoEnvio;
        }

        public String getNombre() {
            return nombre;
        }

        public int getCostoEnvio() {
            return costoEnvio;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}