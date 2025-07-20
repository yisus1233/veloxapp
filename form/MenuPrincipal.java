package form;


import Panel.ClientePanel;
import Panel.MotorizadoPanel;
import Panel.ProductoPanel;
import Panel.PedidoPanel;
import Panel.DetallePedidoPanel;
import Panel.EntregaPanel;
import Panel.ReportePanel;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {
    private JPanel panelContenido; // Panel derecho

    public MenuPrincipal() {
        setTitle("Menú Principal - Velox");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // --------------------------
        // PANEL IZQUIERDO (logo + menú)
        // --------------------------
        JPanel panelMenu = new JPanel(new BorderLayout());
        panelMenu.setBackground(new Color(2, 34, 177));
        panelMenu.setPreferredSize(new Dimension(300, 0)); // Ensanchado

        // LOGO ARRIBA
        ImageIcon icon = new ImageIcon("Panel/LOGO_VELOX.png");
        Image img = icon.getImage().getScaledInstance(210, 110, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(img));
        lblLogo.setHorizontalAlignment(JLabel.CENTER);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(18, 8, 18, 8));
        panelMenu.add(lblLogo, BorderLayout.NORTH);

        // BOTONES MENÚ
        String[] nombres = {
                "Ingresar Cliente", "Ingresar Motorizado", "Ingresar Producto",
                "Ingresar Pedido", "Detalle de Pedido", "Entrega", "Reportes"
        };
        JButton[] botones = new JButton[nombres.length];

        JPanel panelBotones = new JPanel(new GridLayout(nombres.length, 10, 0, 20));
        panelBotones.setOpaque(false);

        for (int i = 0; i < nombres.length; i++) {
            botones[i] = new JButton(nombres[i]);
            botones[i].setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
            botones[i].setBackground(new Color(253, 232, 1, 255));
            botones[i].setFocusPainted(false);
            panelBotones.add(botones[i]);
        }

        JPanel panelBotonesContenedor = new JPanel(new BorderLayout());
        panelBotonesContenedor.setOpaque(false);
        panelBotonesContenedor.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
        panelBotonesContenedor.add(panelBotones, BorderLayout.NORTH);

        panelMenu.add(panelBotonesContenedor, BorderLayout.CENTER);

        // --------------------------
        // PANEL DERECHO (contenido dinámico)
        // --------------------------
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(new Color(253, 248, 1));
        panelContenido.add(
                new JLabel("<html><div style='font-size:22px;color:#22408f;text-align:center;'>Bienvenido al sistema Velox-Lima!</div></html>", JLabel.CENTER),
                BorderLayout.CENTER
        );

        // Layout general
        setLayout(new BorderLayout());
        add(panelMenu, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);

        // ACCIONES DE LOS BOTONES
        botones[0].addActionListener(e -> mostrarPanel(new ClientePanel()));
        botones[1].addActionListener(e -> mostrarPanel(new MotorizadoPanel()));
        botones[2].addActionListener(e -> mostrarPanel(new ProductoPanel()));
        botones[3].addActionListener(e -> mostrarPanel(new PedidoPanel()));
        botones[4].addActionListener(e -> mostrarPanel(new DetallePedidoPanel()));
        botones[5].addActionListener(e -> mostrarPanel(new EntregaPanel()));
        botones[6].addActionListener(e -> mostrarPanel(new ReportePanel())); // ya listo

        // botones[6].addActionListener(e -> mostrarPanel(new ReportesPanel())); // si tienes panel reportes

        // Puedes desactivar el botón de reportes si aún no está listo
        // botones[6].setEnabled(false);
    }

    // Cambiar el panel derecho por el panel recibido
    private void mostrarPanel(JPanel panel) {
        panelContenido.removeAll();
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
