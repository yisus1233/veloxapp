package veloxapp.modelo;

public class Motorizado {
    private String idmotorizado;
    private String nombre;
    private String celular;
    private String placa;

    // Constructor vacío
    public Motorizado() {
    }

    // Constructor con todos los campos
    public Motorizado(String idmotorizado, String nombre, String celular, String placa) {
        this.idmotorizado = idmotorizado;
        this.nombre = nombre;
        this.celular = celular;
        this.placa = placa;
    }

    // Getters y setters
    public String getIdmotorizado() { return idmotorizado; }

    public void setIdmotorizado(String idmotorizado) { this.idmotorizado = idmotorizado; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCelular() { return celular; }

    public void setCelular(String celular) { this.celular = celular; }

    public String getPlaca() { return placa; }

    public void setPlaca(String placa) { this.placa = placa; }
}