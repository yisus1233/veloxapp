package veloxapp.modelo;

public class Producto {
    private String idproducto;
    private String nombre;
    private double precio;
    private String Tamaño;

    // Constructor vacío
    public Producto() {
    }

    // Constructor con todos los campos
    public Producto(String idproducto, String nombre, double precio, String tamaño) {
        this.idproducto = idproducto;
        this.nombre = nombre;
        this.precio = precio;
        this.Tamaño = tamaño;
    }

    // Getters y Setters
    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTamaño() { return Tamaño; }

    public void setTamaño(String tamaño) { this.Tamaño = tamaño; }
}
