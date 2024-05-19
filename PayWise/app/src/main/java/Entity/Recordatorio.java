package Entity;

import android.app.DownloadManager;
import android.database.sqlite.SQLiteQuery;

import java.time.LocalDate;
import java.time.LocalTime;

public class Recordatorio {

    private int id;
    private String nombre;
    private String lista;
    private LocalDate fecha;
    private LocalTime hora;
    private String repetir;

    public Recordatorio(int id, String nombre, String lista, LocalDate fecha, LocalTime hora, String repetir) {
        this.id = id;
        this.nombre = nombre;
        this.lista = lista;
        this.fecha = fecha;
        this.hora = hora;
        this.repetir = repetir;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLista() {
        return lista;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getRepetir() {
        return repetir;
    }

    public void setRepetir(String repetir) {
        this.repetir = repetir;
    }
}
