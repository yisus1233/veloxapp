package veloxapp.modelo;

public class DetallePedido {
    private String iddetalle;
    private String idpedido;
    private String idproducto;
    private int cantidad;
    private double subtotal;

    public DetallePedido() {
    }

    public DetallePedido(String iddetalle, String idpedido, String idproducto, int cantidad, double subtotal) {
        this.iddetalle = iddetalle;
        this.idpedido = idpedido;
        this.idproducto = idproducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public String getIddetalle() { return iddetalle; }

    public void setIddetalle(String iddetalle) { this.iddetalle = iddetalle; }

    public String getIdpedido() { return idpedido; }

    public void setIdpedido(String idpedido) { this.idpedido = idpedido; }

    public String getIdproducto() { return idproducto; }

    public void setIdproducto(String idproducto) { this.idproducto = idproducto; }

    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }

    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}