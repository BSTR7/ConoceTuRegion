package com.example.conoceturegion;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperUsuarios extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "usuarios.db";
    private static final int DATABASE_VERSION = 3;

    // Table names and column names for usuarios
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USUARIO = "usuario";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_APELLIDO = "apellido";
    public static final String COLUMN_CORREO = "correo";
    public static final String COLUMN_CONTRASENA = "contrasena";
    public static final String COLUMN_REGION = "region";

    // Table names and column names for comentarios
    public static final String TABLE_COMENTARIOS = "comentarios";
    public static final String COLUMN_COMENTARIOS_ID = "comentario_id";
    public static final String COLUMN_COMENTARIOS_USUARIO = "comentario_usuario";
    public static final String COLUMN_COMENTARIOS_COMENTARIO = "comentario_texto";
    public static final String COLUMN_COMENTARIOS_UBICACION = "comentario_ubicacion";

    public static final String COLUMN_COMENTARIOS_VALORACION = "comentario_valoracion";

    // Table names and column names for solicitud
    public static final String TABLE_SOLICITUD = "solicitud";
    public static final String COLUMN_SOLICITUD_ID = "solicitud_id";
    public static final String COLUMN_SOLICITUD_NOMBRE = "solicitud_nombre";
    public static final String COLUMN_SOLICITUD_EMAIL = "solicitud_email";
    public static final String COLUMN_SOLICITUD_LATITUD = "solicitud_latitud";
    public static final String COLUMN_SOLICITUD_LONGITUD = "solicitud_longitud";
    public static final String COLUMN_SOLICITUD_ESTADO = "solicitud_estado";


    // SQL statement to create the usuarios table
    private static final String CREATE_TABLE_USUARIOS = "CREATE TABLE " +
            TABLE_USUARIOS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USUARIO + " TEXT, " +
            COLUMN_NOMBRE + " TEXT, " +
            COLUMN_APELLIDO + " TEXT, " +
            COLUMN_CORREO + " TEXT, " +
            COLUMN_CONTRASENA + " TEXT, " +
            COLUMN_REGION + " TEXT" +
            ");";

    // SQL statement to create the comentarios table
    private static final String CREATE_TABLE_COMENTARIOS = "CREATE TABLE " +
            TABLE_COMENTARIOS + "(" +
            COLUMN_COMENTARIOS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_COMENTARIOS_USUARIO + " TEXT, " +
            COLUMN_COMENTARIOS_COMENTARIO + " TEXT CHECK(length(" + COLUMN_COMENTARIOS_COMENTARIO + ") <= 30), " +
            COLUMN_COMENTARIOS_UBICACION + " TEXT, " +
            COLUMN_COMENTARIOS_VALORACION + " TEXT " +
            ");";



    // SQL statement to create the solicitud table
    private static final String CREATE_TABLE_SOLICITUD = "CREATE TABLE " +
            TABLE_SOLICITUD + "(" +
            COLUMN_SOLICITUD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SOLICITUD_NOMBRE + " TEXT, " +
            COLUMN_SOLICITUD_EMAIL + " TEXT, " +
            COLUMN_SOLICITUD_LATITUD + " REAL, " +
            COLUMN_SOLICITUD_LONGITUD + " REAL, " +
            COLUMN_SOLICITUD_ESTADO + " INTEGER" +
            ");";


    public DatabaseHelperUsuarios(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the usuarios and comentarios tables
        db.execSQL(CREATE_TABLE_USUARIOS);
        db.execSQL(CREATE_TABLE_COMENTARIOS);
        db.execSQL(CREATE_TABLE_SOLICITUD);

        Log.d("DatabaseHelperUsuarios", "Tablas creadas exitosamente");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist and recreate the tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMENTARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLICITUD);

        onCreate(db);
    }

    public long insertSolicitud(ObjetoSolicitud solicitud) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOLICITUD_NOMBRE, solicitud.getNombresolicitud());
        values.put(COLUMN_SOLICITUD_EMAIL, solicitud.getEmail());
        values.put(COLUMN_SOLICITUD_LATITUD, solicitud.getLatlngSolicitud().latitude);
        values.put(COLUMN_SOLICITUD_LONGITUD, solicitud.getLatlngSolicitud().longitude);
        values.put(COLUMN_SOLICITUD_ESTADO, solicitud.isEstado() ? 1 : 0);
        long newRowId = db.insert(TABLE_SOLICITUD, null, values);
        db.close();
        return newRowId;
    }

    public long insertUsuario(String usuario, String nombre, String apellido, String correo, String contrasena, String region) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO, usuario);
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_APELLIDO, apellido);
        values.put(COLUMN_CORREO, correo);
        values.put(COLUMN_CONTRASENA, contrasena);
        values.put(COLUMN_REGION, region);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_USUARIOS, null, values);

        // Close the database connection
        db.close();

        return newRowId;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USUARIO + " = ? AND " + COLUMN_CONTRASENA + " = ?";
        String[] selectionArgs = {username, password};
        String limit = "1"; // Limit the result to 1 row

        Cursor cursor = db.query(TABLE_USUARIOS, columns, selection, selectionArgs, null, null, null, limit);

        boolean userExists = cursor.moveToFirst();
        cursor.close();
        db.close();

        return userExists;
    }


    public long insertComentario(String usuario, String comentario, String ubicacion, String valoracion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMENTARIOS_USUARIO, usuario);
        values.put(COLUMN_COMENTARIOS_COMENTARIO, comentario);
        values.put(COLUMN_COMENTARIOS_UBICACION, ubicacion);
        values.put(COLUMN_COMENTARIOS_VALORACION, valoracion);
        long newRowId = db.insert(TABLE_COMENTARIOS, null, values);
        db.close();
        return newRowId;
    }


    // Método para obtener todas las solicitudes

    public ArrayList<ObjetoSolicitud> obtenerTodasSolicitudes() {
        ArrayList<ObjetoSolicitud> solicitudesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_SOLICITUD;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ObjetoSolicitud solicitud = new ObjetoSolicitud();
                solicitud.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SOLICITUD_ID)));
                solicitud.setNombresolicitud(cursor.getString(cursor.getColumnIndex(COLUMN_SOLICITUD_NOMBRE)));
                solicitud.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_SOLICITUD_EMAIL)));
                double latitud = cursor.getDouble(cursor.getColumnIndex(COLUMN_SOLICITUD_LATITUD));
                double longitud = cursor.getDouble(cursor.getColumnIndex(COLUMN_SOLICITUD_LONGITUD));
                LatLng latLng = new LatLng(latitud, longitud);
                solicitud.setLatlngSolicitud(latLng);
                boolean estado = cursor.getInt(cursor.getColumnIndex(COLUMN_SOLICITUD_ESTADO)) == 1;
                solicitud.setEstado(estado);

                solicitudesList.add(solicitud);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return solicitudesList;
    }

    public List<ObjetoSolicitud> obtenerSolicitudesDesdeBD() {
        return obtenerTodasSolicitudes();
    }

    // Método para actualizar el estado de una solicitud
    public int actualizarEstadoSolicitud(int solicitudId, boolean nuevoEstado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOLICITUD_ESTADO, nuevoEstado ? 1 : 0);
        int rowsAffected = db.update(TABLE_SOLICITUD, values, COLUMN_SOLICITUD_ID + " = ?", new String[]{String.valueOf(solicitudId)});
        db.close();

        return rowsAffected;
    }

    // Método para eliminar una solicitud
    public int eliminarSolicitud(int solicitudId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsAffected = db.delete(TABLE_SOLICITUD, COLUMN_SOLICITUD_ID + " = ?", new String[]{String.valueOf(solicitudId)});
        db.close();

        return rowsAffected;
    }

    public ArrayList<ObjetoSolicitud> obtenerSolicitudesHabilitadasConNombre() {
        ArrayList<ObjetoSolicitud> solicitudesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_SOLICITUD + " WHERE " + COLUMN_SOLICITUD_ESTADO + " = 0";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ObjetoSolicitud solicitud = new ObjetoSolicitud();
                solicitud.setNombresolicitud(cursor.getString(cursor.getColumnIndex(COLUMN_SOLICITUD_NOMBRE)));
                double latitud = cursor.getDouble(cursor.getColumnIndex(COLUMN_SOLICITUD_LATITUD));
                double longitud = cursor.getDouble(cursor.getColumnIndex(COLUMN_SOLICITUD_LONGITUD));
                LatLng latLng = new LatLng(latitud, longitud);
                solicitud.setLatlngSolicitud(latLng);

                solicitudesList.add(solicitud);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return solicitudesList;
    }

    // Ver Comentarios

    public ArrayList<String> verComentarios() {
        ArrayList<String> comentariosList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " +
                COLUMN_COMENTARIOS_COMENTARIO + ", " +
                COLUMN_COMENTARIOS_USUARIO + ", " +
                COLUMN_COMENTARIOS_UBICACION + ", " +
                COLUMN_COMENTARIOS_VALORACION +
                " FROM " + TABLE_COMENTARIOS;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String Estrellas = "";
                String comentario = cursor.getString(cursor.getColumnIndex(COLUMN_COMENTARIOS_COMENTARIO));
                String usuario = cursor.getString(cursor.getColumnIndex(COLUMN_COMENTARIOS_USUARIO));
                String ubicacion = cursor.getString(cursor.getColumnIndex(COLUMN_COMENTARIOS_UBICACION));
                String recomendacion = cursor.getString(cursor.getColumnIndex(COLUMN_COMENTARIOS_VALORACION));

                if (recomendacion.equals("Aceptable")) {
                    Estrellas = "⭐";
                } else if (recomendacion.equals("Buena opción")) {
                    Estrellas = "⭐⭐";
                } else if (recomendacion.equals("Excepcionalmente recomendado")) {
                    Estrellas = "⭐⭐⭐";
                }

                String comentarioConUsuarioYUbicacion = "Usuario: " + usuario + "\nUbicación: " + ubicacion + "\nComentario: " + comentario + "\nValoracion: "  + Estrellas;

                comentariosList.add(comentarioConUsuarioYUbicacion);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return comentariosList;
    }

    // Ver Usuarios

    public ArrayList<String> verUsuarios() {
        ArrayList<String> usuariosList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_USUARIO + " FROM " + TABLE_USUARIOS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String usuario = cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO));
                usuariosList.add(usuario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return usuariosList;
    }

    public int eliminarUsuarioPorNombre(String nombreUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_USUARIO + " = ?";
        String[] whereArgs = {nombreUsuario};
        int rowsAffected = db.delete(TABLE_USUARIOS, whereClause, whereArgs);
        db.close();
        return rowsAffected;
    }

}
