package com.yapp.mrrabbit.yapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by michael on 18/10/17.
 */

public class DBTiquetesDisponibles extends SQLiteOpenHelper {

    //int idTiquete, int idCompra, String tipoTiquete, String codigoQR, boolean canjeada
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DBTiquetesDisponibles.db";

    public static final String TABLA_NOMBRES = "Tiquetes";
    public static final String COLUMNA_ID = "id";
    public static final String COLUMNA_ID_TIQUETE = "idTiquete";
    public static final String COLUMNA_TIPO_TIQUETE = "tipoTiquete";
    public static final String COLUMNA_CODIGO_QR = "codigoQR";
    public static final String COLUMNA_CANJEADA = "canjeada";
    public static final String COLUMNA_SINCRNIZADO = "sincronizado";


    private static final String SQL_CREAR  = "create table "
            + TABLA_NOMBRES + "(" + COLUMNA_ID
            + " integer primary key autoincrement, "+ COLUMNA_ID_TIQUETE +" integer not null, "+ COLUMNA_TIPO_TIQUETE +" text not null,"
            + COLUMNA_CODIGO_QR +" text not null,"+ COLUMNA_CANJEADA +" int not null,"
            + COLUMNA_SINCRNIZADO +" int not null);";

    private static final String SQL_CREAR_TABLA_ID_EVENTO = "create table IdEventos (idEvento integer primary key not null);";

    private int idEvento;

    public DBTiquetesDisponibles(Context context, int idEvento) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.idEvento = idEvento;
        if(idEvento!=getIdEventoDB()){
            eliminarBaseDatos(context);
            ingresarIdEvento(idEvento);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR);
        db.execSQL(SQL_CREAR_TABLA_ID_EVENTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //;
    }

    public int agregar(Tiquete tiquete){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMNA_ID_TIQUETE, tiquete.getIdTiquete());
        values.put(COLUMNA_TIPO_TIQUETE, tiquete.getTipoTiquete());
        values.put(COLUMNA_CODIGO_QR, tiquete.getCodigoQR());
        values.put(COLUMNA_CANJEADA, tiquete.isCanjeada());
        values.put(COLUMNA_SINCRNIZADO, tiquete.isSincronizada());

        long newRowId;

        newRowId = db.insert(TABLA_NOMBRES, null,values);
        db.close();
        return (int) newRowId;
    }

    public void eliminarBaseDatos(Context contextoDB){
        contextoDB.deleteDatabase(DATABASE_NAME);
    }

    private int ingresarIdEvento(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idEvento", id);

        long newRowId;


        newRowId = db.insert("IdEventos", null,values);
        return (int) newRowId;
    }

    private int getIdEventoDB(){
        String[] projection = {"idEvento"};
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        Cursor cursor =
                db.query("IdEventos",
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);


        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }
        db.close();
        return id;
    }

    public Tiquete obtenerTiqueteByQR(String qrResult){
        Tiquete escaneado = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_ID_TIQUETE, COLUMNA_TIPO_TIQUETE, COLUMNA_CODIGO_QR, COLUMNA_CANJEADA, COLUMNA_SINCRNIZADO};

        Cursor cursor =
                db.query(TABLA_NOMBRES,
                        projection,
                        " "+COLUMNA_CODIGO_QR+" = ?",
                        new String[] { qrResult },
                        null,
                        null,
                        null,
                        null);


        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            escaneado = new Tiquete(cursor.getInt(1), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4) == 1, cursor.getInt(5) == 1);
        }
        db.close();
        return escaneado;
    }

    public ArrayList<Tiquete> obtenerTodos(){
        ArrayList<Tiquete> tiquetes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_ID_TIQUETE, COLUMNA_TIPO_TIQUETE, COLUMNA_CODIGO_QR, COLUMNA_CANJEADA, COLUMNA_SINCRNIZADO};

        Cursor cursor =
                db.query(TABLA_NOMBRES,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);


        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            do{
                tiquetes.add(new Tiquete(cursor.getInt(1), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4) == 1, cursor.getInt(5) == 1));
            }while(cursor.moveToNext());
        }

        db.close();
        return tiquetes;
    }

    public int setCanjeado (int idtiquete){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMNA_CANJEADA, 1);

        int i = -1;

        i = db.update(TABLA_NOMBRES,
                values,
                " "+COLUMNA_ID_TIQUETE+" = ?",
                new String[] { String.valueOf( idtiquete ) });
        db.close();

        return i;
    }

}
