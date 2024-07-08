package Entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Deuda {
    private String empresa;
    private String estado;
    private LocalDate fecha;
    private LocalTime hora;
    private String id;
    private float monto;
    private String tipo;
    private byte[] imagen;

    public Deuda() {
    }

    public Deuda(String empresa, String estado, LocalDate fecha, LocalTime hora, String id, float monto, String tipo, byte[] imagen) {
        this.empresa = empresa;
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.id = id;
        this.monto = monto;
        this.tipo = tipo;
        this.imagen = imagen;
    }

    public String getId() {
        return this.id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getEmpresa() {
        return this.empresa;
    }

    public float getMonto() {
        return this.monto;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public LocalTime getHora() {
        return this.hora;
    }

    public String getEstado() {
        return this.estado;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public void setTipo(String tipo2) {
        this.tipo = tipo2;
    }

    public void setEmpresa(String empresa2) {
        this.empresa = empresa2;
    }

    public void setMonto(float monto2) {
        this.monto = monto2;
    }

    public void setFecha(LocalDate fecha2) {
        this.fecha = fecha2;
    }

    public void setHora(LocalTime hora2) {
        this.hora = hora2;
    }

    public void setEstado(String estado2) {
        this.estado = estado2;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}