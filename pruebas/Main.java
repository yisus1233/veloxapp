package pruebas;

import veloxapp.form.ClienteForm;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo aplicar el estilo visual.");
        }

        ClienteForm ventana = new ClienteForm();
        ventana.setVisible(true);
    }
}
