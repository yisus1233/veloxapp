package veloxapp.form;

import veloxapp.modelo.Producto;
import veloxapp.manager.ProductoManager;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel principal
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

        // Agregar al panel
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
        panel.add(new JLabel());
        panel.add(btnCerrar);

        // Lógica
        ProductoManager manager = new ProductoManager();
        txtId.setText(manager.generarNuevoIdProducto());

        btnRegistrar.addActionListener(e -> {
            try {
                Producto p = new Producto();
                p.setIdproducto(txtId.getText());
                p.setNombre(txtNombre.getText());
                p.setPrecio(new BigDecimal(txtPrecio.getText()));
                p.setStock(Integer.parseInt(txtStock.getText()));

                if (manager.insertarProducto(p)) {
                    JOptionPane.showMessageDialog(this, "Producto registrado con éxito");
                    limpiarCampos();
                    txtId.setText(manager.generarNuevoIdProducto());
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar producto");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage());
            }
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnCerrar.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
    }

    public static void main(String[] args) {
        new ProductoForm();
    }
}
