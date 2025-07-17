package veloxapp.form;

import veloxapp.manager.ProductoManager;
import veloxapp.modelo.Producto;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class ProductoForm extends JFrame {

    private final JTextField txtId, txtNombre, txtPrecio, txtStock;
    private final JButton btnRegistrar, btnLimpiar, btnCerrar;

    public ProductoForm() {
        setTitle("Registro de Producto");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campos
        txtId = new JTextField();
        txtId.setEditable(false);
        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();

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
        panel.add(new JLabel("Stock:"));
        panel.add(txtStock);
        panel.add(btnRegistrar);
        panel.add(btnLimpiar);
        panel.add(btnCerrar);

        add(panel);

        // Valores automáticos
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
        String nuevoId = manager.generarNuevoId();
        txtId.setText(nuevoId);
    }

    private void registrarProducto() {
        try {
            Producto producto = new Producto();
            producto.setIdproducto(txtId.getText());
            producto.setNombre(txtNombre.getText());
            producto.setPrecio(new BigDecimal(txtPrecio.getText()));
            producto.setStock(Integer.parseInt(txtStock.getText()));

            ProductoManager manager = new ProductoManager();
            boolean exito = manager.insertarProducto(producto);

            JOptionPane.showMessageDialog(this, "✅ Producto registrado con éxito.");
            limpiarCampos();
            generarNuevoId();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Error al registrar producto: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
    }
}
