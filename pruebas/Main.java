package veloxapp.pruebas;

import veloxapp.form.DetallePedidoForm;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            // Usar el estilo del sistema operativo
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo aplicar el estilo visual.");
        }

        DetallePedidoForm ventana = new DetallePedidoForm();
        ventana.setVisible(true);
    }
}