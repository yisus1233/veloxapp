package veloxapp.modelo;

public class Pedido {
    private String idpedido;
    private String idcliente;
    private String fechapedido;
    private String estado;
    private double total;

    public Pedido() {}

    public Pedido(String idpedido, String idcliente, String fechapedido, String estado, double total) {
        this.idpedido = idpedido;
        this.idcliente = idcliente;
        this.fechapedido = fechapedido;
        this.estado = estado;
        this.total = total;
    }

    public String getIdpedido() { return idpedido; }

    public void setIdpedido(String idpedido) { this.idpedido = idpedido; }

    public String getIdcliente() { return idcliente; }

    public void setIdcliente(String idcliente) { this.idcliente = idcliente; }

    public String getFechapedido() { return fechapedido; }

    public void setFechapedido(String fechapedido) { this.fechapedido = fechapedido; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public double getTotal() { return total; }

    public void setTotal(double total) { this.total = total; }
}