package veloxapp.modelo;

public class Producto {
    private String idproducto;
    private String nombre;
    private double precio;
    private int stock;

    // Constructor vacío
    public Producto() {
    }

    // Constructor con todos los campos
    public Producto(String idproducto, String nombre, double precio, int stock) {
        this.idproducto = idproducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
