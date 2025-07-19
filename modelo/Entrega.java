package veloxapp.modelo;

import java.sql.Date;
import java.sql.Time;

public class Entrega {
    private String identrega;
    private String idpedido;
    private String idmotorizado;
    private Date fechaentrega;
    private Time horaentrega;
    private String estado;

    public Entrega() {}

    public Entrega(String identrega, String idpedido, String idmotorizado, Date fechaentrega, Time horaentrega, String estado) {
        this.identrega = identrega;
        this.idpedido = idpedido;
        this.idmotorizado = idmotorizado;
        this.fechaentrega = fechaentrega;
        this.horaentrega = horaentrega;
        this.estado = estado;
    }

    public String getIdentrega() { return identrega; }
    public void setIdentrega(String identrega) { this.identrega = identrega; }

    public String getIdpedido() { return idpedido; }
    public void setIdpedido(String idpedido) { this.idpedido = idpedido; }

    public String getIdmotorizado() { return idmotorizado; }
    public void setIdmotorizado(String idmotorizado) { this.idmotorizado = idmotorizado; }

    public Date getFechaentrega() { return fechaentrega; }
    public void setFechaentrega(Date fechaentrega) { this.fechaentrega = fechaentrega; }

    public Time getHoraentrega() { return horaentrega; }
    public void setHoraentrega(Time horaentrega) { this.horaentrega = horaentrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
