package pruebas;

import veloxapp.form.EntregaForm;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo aplicar el estilo visual.");
        }

        EntregaForm ventana = new EntregaForm();
        ventana.setVisible(true);
    }
}
