package pruebas;

import form.MenuPrincipal;
import javax.swing.UIManager;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo aplicar el estilo visual.");
        }

        MenuPrincipal ventana = new MenuPrincipal();
        ventana.setVisible(true);
    }
}
