package veloxapp;

import veloxapp.form.ClienteForm;

public class main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            ClienteForm form = new ClienteForm();
            form.setVisible(true);
        });
    }
}
