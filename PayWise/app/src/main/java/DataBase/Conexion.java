package DataBase;

import Entity.Deuda;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Conexion extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DE_DATOS = "PayWise.db";
    private static final int VERSION_BASE_DE_DATOS = 1;

    public Conexion(Context context) {
        super(context, NOMBRE_BASE_DE_DATOS, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {

        String sqlCrearTabla = "CREATE TABLE deudas ("+
                "id TEXT NOT NULL UNIQUE PRIMARY KEY, "+
                "tipo TEXT NOT NULL, "+
                "empresa TEXT NOT NULL, "+
                "monto TEXT NOT NULL, "+
                "fecha TEXT NOT NULL, "+
                "hora TEXT NOT NULL, "+
                "estado TEXT NOT NULL, "+
                "archivo BLOB)";

        db.execSQL(sqlCrearTabla);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS deudas");
        onCreate(db);
    }

    public void ingresarDatos(Context context, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("deudas", null, values);
        db.close();
    }

    public ArrayList<Deuda> obtenerIds() {
        ArrayList<Deuda> deudas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM deudas", null);
        int idIndex = cursor.getColumnIndex("id");
        int empresaIndex = cursor.getColumnIndex("empresa");
        int montoIndex = cursor.getColumnIndex("monto");
        int fechaIndex = cursor.getColumnIndex("fecha");
        int horaIndex = cursor.getColumnIndex("hora");
        int tipoIndex = cursor.getColumnIndex("tipo");
        int estadoIndex = cursor.getColumnIndex("estado");
        DateTimeFormatter dateFormatterBD = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (cursor.moveToFirst()) {
            do {
                Deuda deuda = new Deuda();
                deuda.setId(cursor.getString(idIndex));
                deuda.setEmpresa(cursor.getString(empresaIndex));
                deuda.setTipo(cursor.getString(tipoIndex));
                deuda.setMonto(cursor.getFloat(montoIndex));
                deuda.setFecha(LocalDate.parse(cursor.getString(fechaIndex), dateFormatterBD));
                deuda.setHora(LocalTime.parse(cursor.getString(horaIndex)));
                deuda.setEstado(cursor.getString(estadoIndex));
                deudas.add(deuda);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return deudas;
    }

    public ArrayList<Deuda> obtenerDeudasPorMes(String mes) {
        ArrayList<Deuda> deudas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM deudas", null);
        int idIndex = cursor.getColumnIndex("id");
        int empresaIndex = cursor.getColumnIndex("empresa");
        int montoIndex = cursor.getColumnIndex("monto");
        int fechaIndex = cursor.getColumnIndex("fecha");
        int horaIndex = cursor.getColumnIndex("hora");
        int tipoIndex = cursor.getColumnIndex("tipo");
        int estadoIndex = cursor.getColumnIndex("estado");
        DateTimeFormatter dateFormatterBD = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (cursor.moveToFirst()) {
            do {
                Deuda deuda = new Deuda();
                deuda.setId(cursor.getString(idIndex));
                deuda.setEmpresa(cursor.getString(empresaIndex));
                deuda.setTipo(cursor.getString(tipoIndex));
                deuda.setMonto(cursor.getFloat(montoIndex));
                deuda.setFecha(LocalDate.parse(cursor.getString(fechaIndex), dateFormatterBD));
                deuda.setHora(LocalTime.parse(cursor.getString(horaIndex)));
                deuda.setEstado(cursor.getString(estadoIndex));
                YearMonth mesSeleccionado = YearMonth.of(LocalDate.now().getYear(), Integer.parseInt(mes));
                if (deuda.getFecha().getMonthValue() == Integer.parseInt(mes) || (deuda.getEstado().equals("Por pagar") && YearMonth.from(deuda.getFecha()).compareTo(mesSeleccionado) <= 0)) {
                    deudas.add(deuda);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return deudas;
    }

    public void eliminarDeuda(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("deudas", "id=?", new String[]{id});
        db.close();
    }

    public Cursor buscarDeudasPorId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM deudas WHERE id= '" + id + "'", null);

        return cursor;
    }

    public void actualizarDeuda(String id, ContentValues values) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.update("deudas", values, "id = ?", new String[]{id});
        db.close();
    }

    public boolean existeId(String id){
        boolean existe = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM deudas WHERE id= '"+id+"'", null);

        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex("id");
            if(!cursor.getString(idIndex).isEmpty()){
                existe = true;
            }
        }
        return existe;
    }
}