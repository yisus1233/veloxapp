package veloxapp.form;

import veloxapp.modelo.Producto;
import veloxapp.manager.ProductoManager;

import javax.swing.*;
import java.awt.*;

public class ProductoForm extends JFrame {

    private final JTextField txtId, txtNombre, txtPrecio, txtTamaño;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar;

    public ProductoForm() {
        setTitle("Registro de Producto");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Crear campos
        txtId = new JTextField();
        txtId.setEditable(false);

        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtTamaño = new JTextField();

        // Botones
        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrar = new JButton("Cerrar");

        // Añadir componentes
        panel.add(new JLabel("ID Producto:"));
        panel.add(txtId);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Precio:"));
        panel.add(txtPrecio);
        panel.add(new JLabel("Tamaño:"));
        panel.add(txtTamaño);
        panel.add(btnRegistrar);
        panel.add(btnLimpiar);
        panel.add(new JLabel()); // Espacio vacío
        panel.add(btnCerrar);

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
            producto.setTamaño(txtTamaño.getText());

            ProductoManager manager = new ProductoManager();
            boolean exito = manager.registrarProducto(producto);

            if (exito) {
                JOptionPane.showMessageDialog(this, "✅ Producto registrado con éxito.");
                limpiarCampos();
                generarNuevoId();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Ingrese valores válidos en precio y tamaño.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtTamaño.setText("");
    }
}
