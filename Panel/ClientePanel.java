package Panel;

import veloxapp.modelo.Cliente;
import veloxapp.manager.ClienteManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ClientePanel extends JPanel {

    private final JTextField txtId, txtNombre, txtDireccion, txtCelular, txtFecha;
    private final JComboBox<String> comboTienda;
    private final JComboBox<DistritoItem> comboDistrito;
    private final JButton btnRegistrar, btnLimpiar;

    private boolean clienteRegistrado = false;

    private final String[] tiendas = {
            "AMATE", "ATLANTIS", "BEAUTY", "FITBELLA", "HND", "LIBERTY", "MEN LAD", "RIVAS", "THIFAS",
            "TUMI SOF", "WONDER", "FLOWER", "MARABELL", "VITELA", "ZASSY", "2BLEA", "TAWAQUI", "QUEEN",
            "CIOSNI", "TIMUY"
    };

    private final DistritoItem[] distritos = {
            new DistritoItem("AGUSTINO", 8), new DistritoItem("ATE", 8),
            new DistritoItem("BREÑA", 8), new DistritoItem("CALLAO", 8),
            new DistritoItem("CARABAYLLO", 12), new DistritoItem("CHORRILLOS", 8),
            new DistritoItem("COMAS", 8), new DistritoItem("INDEPENDENCIA", 8),
            new DistritoItem("JESUS MARIA", 8), new DistritoItem("LIMA", 8),
            new DistritoItem("MAGDALENA", 8), new DistritoItem("MANCHAY", 18),
            new DistritoItem("MIRAFLORES", 8), new DistritoItem("RIMAC", 8),
            new DistritoItem("SAN ISIDRO", 8), new DistritoItem("SAN MIGUEL", 8),
            new DistritoItem("SJL REGULAR", 8), new DistritoItem("LA MOLINA", 8),
            new DistritoItem("SURCO", 8), new DistritoItem("SURQUILLO", 8),
            new DistritoItem("VENTANILLA", 15)
    };

    public ClientePanel() {
        // Estilo general
        setBackground(new Color(245, 247, 255));
        setLayout(new GridBagLayout());

        // Panel central del formulario, con sombra
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30, 60, 30, 60),
                BorderFactory.createLineBorder(new Color(180, 200, 240), 1)
        ));
        formPanel.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 18, 12, 18);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 15);

        txtId = new JTextField(15); txtId.setEditable(false); txtId.setFont(inputFont);
        txtNombre = new JTextField(15); txtNombre.setFont(inputFont);
        txtDireccion = new JTextField(15); txtDireccion.setFont(inputFont);
        txtCelular = new JTextField(15); txtCelular.setFont(inputFont);
        txtFecha = new JTextField(15); txtFecha.setEditable(false); txtFecha.setFont(inputFont);

        comboTienda = new JComboBox<>(tiendas); comboTienda.setFont(inputFont);
        comboDistrito = new JComboBox<>(distritos); comboDistrito.setFont(inputFont);

        btnRegistrar = new JButton("Registrar"); btnRegistrar.setFont(inputFont);
        btnLimpiar = new JButton("Limpiar"); btnLimpiar.setFont(inputFont);

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("ID Cliente:") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 1;
        formPanel.add(txtId, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Nombre:") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 3;
        formPanel.add(txtNombre, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Tienda:") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 1;
        formPanel.add(comboTienda, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Distrito:") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 3;
        formPanel.add(comboDistrito, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Dirección:") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 1;
        formPanel.add(txtDireccion, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Celular:") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 3;
        formPanel.add(txtCelular, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Fecha de Registro:") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 1;
        formPanel.add(txtFecha, gbc);

        // Botones centrados abajo
        gbc.gridy = ++y;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btns = new JPanel();
        btns.setBackground(Color.white);
        btns.add(btnRegistrar);
        btns.add(btnLimpiar);
        formPanel.add(btns, gbc);

        add(formPanel, new GridBagConstraints()); // Panel centrado

        generarNuevoId();
        cargarFechaActual();

        btnRegistrar.addActionListener(e -> registrarCliente());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
            cargarFechaActual();
            clienteRegistrado = false;
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

    public static class DistritoItem {
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
