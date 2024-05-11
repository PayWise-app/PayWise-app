package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "hora TEXT,"+
                "repetir TEXT NOT NULL)";
        db.execSQL(sqlCrearTabla);
    }

    public void ingresarDatos(Context context, ContentValues values){

        Conexion database = new Conexion(context);
        SQLiteDatabase db = database.getWritableDatabase();

        long idNuevo = db.insert("usuarios", null, values);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Manejar actualizaciones de la base de datos aquí
        // Por simplicidad, simplemente eliminamos y recreamos la tabla
        db.execSQL("DROP TABLE IF EXISTS recordatorios");
        onCreate(db);
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

}
