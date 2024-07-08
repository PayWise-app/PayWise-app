package Entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Deuda {
    private byte[] archivo;
    private String empresa;
    private String estado;
    private LocalDate fecha;
    private LocalTime hora;
    private String id;
    private float monto;
    private String tipo;

    public Deuda() {
    }

    public Deuda(String id2, String tipo2, String empresa2, float monto2, LocalDate fecha2, LocalTime hora2, String estado2, byte[] archivo2) {
        this.id = id2;
        this.tipo = tipo2;
        this.empresa = empresa2;
        this.monto = monto2;
        this.fecha = fecha2;
        this.hora = hora2;
        this.estado = estado2;
        this.archivo = archivo2;
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

    public byte[] getArchivo() {
        return this.archivo;
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

    public void setArchivo(byte[] archivo2) {
        this.archivo = archivo2;
    }
}