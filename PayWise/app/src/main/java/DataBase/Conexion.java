package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.*;


public class Conexion extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DE_DATOS = "PayWise.db";
    private static final int VERSION_BASE_DE_DATOS = 1;
    public Conexion(Context context) {
        super(context, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla cuando se crea la base de datos por primera vez
        String sqlCrearTabla = "CREATE TABLE recordatorios (" +
                "id INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "lista TEXT NOT NULL," +
                "fecha TEXT NOT NULL," +
                "hora TEXT NOT NULL)";
        db.execSQL(sqlCrearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se ejecuta si la versión de la base de datos cambia
        // Aquí puedes realizar tareas de migración o simplemente descartar la tabla y crear una nueva
        db.execSQL("DROP TABLE IF EXISTS recordatorios");
        onCreate(db);
    }

    public void ingresarDatos(Context context, ContentValues values){

        Conexion database = new Conexion(context);
        SQLiteDatabase db = database.getWritableDatabase();

        long idNuevo = db.insert("recordatorios", null, values);

        db.close();
    }

    public Cursor obtenerTodosLosRecordatorios() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM recordatorios";

        return db.rawQuery(query, null);
    }

    public Cursor mostrarPorLista(String lista){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM recordatorios WHERE lista ='"+lista+"'";
        return db.rawQuery(query, null);
    }

    public ArrayList<String> obtenerNombres() {
        ArrayList<String> nombres = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM recordatorios", null);

        if (cursor.moveToFirst()) {
            do {
                nombres.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return nombres;
    }

    public void eliminarRecordatorio(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("recordatorios", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor buscarRecordatorioPorNombre(String nombre){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM recordatorios WHERE nombre= '"+nombre+"'", null);
        return cursor;
    }

    public void actualizarRecordatorio(String nombre, ContentValues values){
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = "nombre = ?";
        String[] whereArgs = {nombre};
        db.update("recordatorios", values, whereClause, whereArgs);
        db.close();
    }

    public ArrayList<String> obtenerNombresPorLista(String lista) {
        ArrayList<String> nombres = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM recordatorios WHERE lista= '"+lista+"'", null);

        if (cursor.moveToFirst()) {
            do {
                nombres.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return nombres;
    }

}
