package veloxapp.modelo;

public class Cliente {
    private String idcliente;
    private String nombre;
    private String ruc;
    private String direccion;
    private String celular;
    private String fecharegistro;

    public Cliente() {
    }

    public Cliente(String idcliente, String nombre, String ruc, String direccion, String celular, String fecharegistro) {
        this.idcliente = idcliente;
        this.nombre = nombre;
        this.ruc = ruc;
        this.direccion = direccion;
        this.celular = celular;
        this.fecharegistro = fecharegistro;
    }

    public String getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(String idcliente) {
        this.idcliente = idcliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(String fecharegistro) {
        this.fecharegistro = fecharegistro;
    }
}
