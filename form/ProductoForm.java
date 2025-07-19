package veloxapp.form;

import veloxapp.modelo.Producto;
import veloxapp.manager.ProductoManager;

import javax.swing.*;
import java.awt.*;

public class ProductoForm extends JFrame {

    private final JTextField txtId, txtNombre, txtPrecio;
    private final JComboBox<TamañoItem> comboTamaño;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar;

    public ProductoForm() {
        setTitle("Registro de Producto");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campos
        txtId = new JTextField(); txtId.setEditable(false);
        txtNombre = new JTextField();
        txtPrecio = new JTextField(); // editable, como tú indicas

        // ComboBox con valores internos ocultos
        comboTamaño = new JComboBox<>(new TamañoItem[] {
                new TamañoItem("Pequeño", 1.0),
                new TamañoItem("Mediano", 1.2),
                new TamañoItem("Grande", 1.5)
        });

        // Botones
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");

        // Añadir componentes al panel
        panel.add(new JLabel("ID Producto:")); panel.add(txtId);
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Precio:")); panel.add(txtPrecio);
        panel.add(new JLabel("Tamaño:")); panel.add(comboTamaño);
        panel.add(btnRegistrar); panel.add(btnLimpiar);
        panel.add(new JLabel()); panel.add(btnCerrar);

        add(panel);

        generarNuevoId();

        // Acciones
        btnRegistrar.addActionListener(e -> registrarProducto());
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            generarNuevoId();
        });
        btnCerrar.addActionListener(e -> dispose());
    }

    private void generarNuevoId() {
        ProductoManager manager = new ProductoManager();
        String nuevoId = manager.generarNuevoIdProducto();
        txtId.setText(nuevoId);
    }

    private void registrarProducto() {
        try {
            Producto producto = new Producto();
            producto.setIdproducto(txtId.getText());
            producto.setNombre(txtNombre.getText());
            producto.setPrecio(Double.parseDouble(txtPrecio.getText()));

            TamañoItem tamañoSeleccionado = (TamañoItem) comboTamaño.getSelectedItem();
            if (tamañoSeleccionado != null) {
                producto.setTamaño(tamañoSeleccionado.getNombre()); // Se guarda como "Pequeño", "Mediano", etc.
            }

            ProductoManager manager = new ProductoManager();
            boolean exito = manager.registrarProducto(producto);

            if (exito) {
                JOptionPane.showMessageDialog(this, "✅ Producto registrado con éxito.");
                limpiarCampos();
                generarNuevoId();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Ingrese un precio válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        comboTamaño.setSelectedIndex(0);
    }

    // Clase interna para representar Tamaño con valor oculto
    private static class TamañoItem {
        private final String nombre;
        private final double multiplicador;

        public TamañoItem(String nombre, double multiplicador) {
            this.nombre = nombre;
            this.multiplicador = multiplicador;
        }

        public String getNombre() {
            return nombre;
        }

        public double getMultiplicador() {
            return multiplicador;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}