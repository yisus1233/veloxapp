package Panel;

import veloxapp.conexion.conexionBD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportePanel extends JPanel {

    private final JComboBox<String> comboTipoReporte;
    private final JPanel panelFiltros;
    private final JTable tablaReporte;
    private final DefaultTableModel modeloTabla;

    // Filtros comunes
    private final JTextField txtBuscar;
    private final JComboBox<String> comboEstado;
    private final JSpinner spinnerFechaInicio, spinnerFechaFin;
    private final JButton btnBuscar;

    public ReportePanel() {
        setBackground(new Color(247, 249, 255));
        setLayout(new BorderLayout(0, 0));

        // --------- BARRA Y TÍTULO SUPERIOR ----------
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(34, 64, 143));
        barra.setPreferredSize(new Dimension(100, 54));
        JLabel lblTitulo = new JLabel("Reportes del Sistema", JLabel.LEFT);
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 23));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(0, 22, 0, 0));
        barra.add(lblTitulo, BorderLayout.WEST);
        add(barra, BorderLayout.NORTH);

        // --------- PANEL ARRIBA (Combo y filtros) ----------
        JPanel panelArriba = new JPanel(new BorderLayout(12, 0));
        panelArriba.setBackground(new Color(247, 249, 255));
        panelArriba.setBorder(new EmptyBorder(30, 20, 0, 20));

        comboTipoReporte = new JComboBox<>(new String[]{
                "Pedidos", "Clientes", "Entregas", "Motorizados", "Productos"
        });
        comboTipoReporte.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        comboTipoReporte.setPreferredSize(new Dimension(180, 34));

        panelArriba.add(comboTipoReporte, BorderLayout.WEST);


        // Panel para filtros dinámicos
        panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelFiltros.setBackground(new Color(247, 249, 255));
        panelArriba.add(panelFiltros, BorderLayout.CENTER);

        add(panelArriba, BorderLayout.PAGE_START);

        // --------- TABLA DE REPORTE ----------
        modeloTabla = new DefaultTableModel();
        tablaReporte = new JTable(modeloTabla);
        tablaReporte.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        tablaReporte.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        tablaReporte.setRowHeight(28);
        tablaReporte.setGridColor(new Color(220, 220, 240));
        tablaReporte.setShowGrid(true);

        JScrollPane scrollTabla = new JScrollPane(tablaReporte);
        scrollTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(16, 26, 16, 26),
                BorderFactory.createLineBorder(new Color(210, 210, 225), 1, true)
        ));

        add(scrollTabla, BorderLayout.CENTER);

        // --------- FILTROS ---------
        txtBuscar = new JTextField(14);
        txtBuscar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        comboEstado = new JComboBox<>(new String[]{"Todos", "Recepcionado", "En ruta", "Entregado", "Cancelado"});
        comboEstado.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        spinnerFechaInicio = new JSpinner(new SpinnerDateModel());
        spinnerFechaFin = new JSpinner(new SpinnerDateModel());
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(34, 64, 143));
        btnBuscar.setForeground(Color.YELLOW);
        btnBuscar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));

        // --------- EVENTOS ---------
        comboTipoReporte.addActionListener(e -> actualizarFiltros());
        btnBuscar.addActionListener(e -> cargarReporte());

        // Filtros por defecto
        actualizarFiltros();
    }

    private void actualizarFiltros() {
        panelFiltros.removeAll();

        String tipo = (String) comboTipoReporte.getSelectedItem();
        panelFiltros.setOpaque(false);

        if ("Pedidos".equals(tipo)) {
            panelFiltros.add(etiqueta("Estado:"));
            panelFiltros.add(comboEstado);

            // Botón grande y separado
            btnBuscar.setPreferredSize(new Dimension(120, 34));
            panelFiltros.add(Box.createHorizontalStrut(25)); // Espacio
            panelFiltros.add(btnBuscar);
        } else if ("Entregas".equals(tipo)) {
            panelFiltros.add(etiqueta("Estado:"));
            panelFiltros.add(comboEstado);
            // Si quieres dejar búsqueda por fechas en entregas, lo dejas aquí. Si no, lo quitas también.
            btnBuscar.setPreferredSize(new Dimension(120, 34));
            panelFiltros.add(Box.createHorizontalStrut(25));
            panelFiltros.add(btnBuscar);
        } else {
            panelFiltros.add(etiqueta("Buscar:"));
            panelFiltros.add(txtBuscar);
            btnBuscar.setPreferredSize(new Dimension(120, 34));
            panelFiltros.add(Box.createHorizontalStrut(25));
            panelFiltros.add(btnBuscar);
        }

    }

    // Etiquetas con estilo
    private JLabel etiqueta(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        l.setForeground(new Color(34, 64, 143));
        return l;
    }
    private java.sql.Date getSpinnerDate(JSpinner spinner) {
        try {
            java.util.Date date = (java.util.Date) spinner.getValue();
            if (date != null) {
                // Solo devuelve la parte de la fecha, sin hora
                return new java.sql.Date(date.getTime());
            }
        } catch (Exception e) {
            // Si hay error, retorna null (no filtra por fecha)
        }
        return null;
    }


    private void cargarReporte() {
        modeloTabla.setRowCount(0); // limpia tabla
        modeloTabla.setColumnCount(0); // limpia columnas

        String tipo = (String) comboTipoReporte.getSelectedItem();

        try (Connection conn = conexionBD.conectar()) {
            PreparedStatement ps = null;
            ResultSet rs = null;

            if ("Pedidos".equals(tipo)) {
                modeloTabla.setColumnIdentifiers(new String[]{"ID", "Cliente", "Fecha", "Estado", "Total"});
                String sql = "SELECT P.idpedido, C.nombre, P.fecha, P.estado, P.total FROM Pedido P JOIN Cliente C ON P.idcliente = C.idcliente WHERE 1=1";
                String estado = (String) comboEstado.getSelectedItem();
                if (!"Todos".equals(estado)) sql += " AND P.estado = ?";
                ps = conn.prepareStatement(sql);
                int idx = 1;
                if (!"Todos".equals(estado)) ps.setString(idx++, estado);

                rs = ps.executeQuery();
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{
                            rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5)
                    });
                }
            } else if ("Clientes".equals(tipo)) {
                modeloTabla.setColumnIdentifiers(new String[]{"ID", "Nombre", "Tienda", "Distrito", "Celular", "Fecha"});
                String sql = "SELECT idcliente, nombre, tienda, distrito, celular, fecharegistro FROM Cliente WHERE nombre LIKE ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + txtBuscar.getText() + "%");
                rs = ps.executeQuery();
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{
                            rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)
                    });
                }
            }
            else if ("Motorizados".equals(tipo)) {
                modeloTabla.setColumnIdentifiers(new String[]{"ID", "Nombre", "Celular", "Placa"});
                String sql = "SELECT idmotorizado, nombre, celular, placa FROM Motorizado WHERE nombre LIKE ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + txtBuscar.getText() + "%");
                rs = ps.executeQuery();
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{
                            rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)
                    });
                }
            }
            else if ("Productos".equals(tipo)) {
                modeloTabla.setColumnIdentifiers(new String[]{"ID", "Nombre", "Tamaño", "Precio"});
                String sql = "SELECT idproducto, nombre, tamaño, precio FROM Producto WHERE nombre LIKE ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + txtBuscar.getText() + "%");
                rs = ps.executeQuery();
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{
                            rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4)
                    });
                }
            }
            else if ("Entregas".equals(tipo)) {
                modeloTabla.setColumnIdentifiers(new String[]{"ID", "Pedido", "Motorizado", "Fecha", "Hora", "Estado"});
                String sql = "SELECT identrega, idpedido, idmotorizado, fechaentrega, horaentrega, estado FROM Entrega WHERE 1=1";

                // Puedes agregar filtros por estado/fecha si quieres
                String estado = (String) comboEstado.getSelectedItem();
                if (!"Todos".equals(estado)) sql += " AND estado = ?";

                java.sql.Date fechaIni = getSpinnerDate(spinnerFechaInicio);
                java.sql.Date fechaFin = getSpinnerDate(spinnerFechaFin);
                if (fechaIni != null) sql += " AND fechaentrega >= ?";
                if (fechaFin != null) sql += " AND fechaentrega <= ?";

                ps = conn.prepareStatement(sql);

                int idx = 1;
                if (!"Todos".equals(estado)) ps.setString(idx++, estado);
                if (fechaIni != null) ps.setDate(idx++, fechaIni);
                if (fechaFin != null) ps.setDate(idx++, fechaFin);

                rs = ps.executeQuery();
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{
                            rs.getString(1), rs.getString(2), rs.getString(3),
                            rs.getDate(4), rs.getTime(5), rs.getString(6)
                    });
                }
            }





        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar reporte: " + e.getMessage());
        }
    }
}
