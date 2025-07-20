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
        setBackground(new Color(245, 247, 255));
        setLayout(new GridBagLayout());

        // Card central blanco con borde y sombra
        JPanel card = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255,255,255, 250));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(690, 280));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 64, 143), 1, true),
                BorderFactory.createEmptyBorder(28, 36, 28, 36))
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 16, 10, 16);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Título, subtítulo y línea decorativa ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JLabel lblTitulo = new JLabel("🧑‍💼 Registro de Cliente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(34, 64, 143));
        card.add(lblTitulo, gbc);

        gbc.gridy++;
        JLabel lblSub = new JLabel("Agrega y gestiona tus clientes rápido y fácil. ¡No olvides llenar todos los campos!");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(80, 120, 180));
        card.add(lblSub, gbc);

        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(210, 210, 240));
        sep.setPreferredSize(new Dimension(540, 2));
        card.add(sep, gbc);

        // --- Campos del formulario ---
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 1: ID y Nombre
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("ID Cliente:", "🔖"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(14); txtId.setEditable(false); txtId.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(txtId, gbc);

        gbc.gridx = 2;
        card.add(labelIcon("Nombre:", "👤"), gbc);
        gbc.gridx = 3;
        txtNombre = new JTextField(14); txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(txtNombre, gbc);

        // Fila 2: Tienda y Distrito
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Tienda:", "🏬"), gbc);
        gbc.gridx = 1;
        comboTienda = new JComboBox<>(tiendas); comboTienda.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(comboTienda, gbc);

        gbc.gridx = 2;
        card.add(labelIcon("Distrito:", "📍"), gbc);
        gbc.gridx = 3;
        comboDistrito = new JComboBox<>(distritos); comboDistrito.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(comboDistrito, gbc);

        // Fila 3: Dirección y Celular
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Dirección:", "🏠"), gbc);
        gbc.gridx = 1;
        txtDireccion = new JTextField(14); txtDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(txtDireccion, gbc);

        gbc.gridx = 2;
        card.add(labelIcon("Celular:", "📱"), gbc);
        gbc.gridx = 3;
        txtCelular = new JTextField(14); txtCelular.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(txtCelular, gbc);

        // Fila 4: Fecha
        gbc.gridy++; gbc.gridx = 0;
        card.add(labelIcon("Fecha de Registro:", "📅"), gbc);
        gbc.gridx = 1;
        txtFecha = new JTextField(14); txtFecha.setEditable(false); txtFecha.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(txtFecha, gbc);

        // Botones centrados
        gbc.gridx = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBtns = new JPanel();
        panelBtns.setOpaque(false);
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        panelBtns.add(btnRegistrar);
        panelBtns.add(btnLimpiar);
        card.add(panelBtns, gbc);

        // Centrado general
        GridBagConstraints gbcRoot = new GridBagConstraints();
        gbcRoot.gridx = 0; gbcRoot.gridy = 0; gbcRoot.weightx = 1; gbcRoot.weighty = 1;
        gbcRoot.anchor = GridBagConstraints.CENTER;
        add(card, gbcRoot);

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

    private JLabel labelIcon(String texto, String emoji) {
        JLabel lbl = new JLabel(emoji + " " + texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setForeground(new Color(45, 70, 120));
        return lbl;
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

    // --------- Clase interna DistritoItem ----------
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
