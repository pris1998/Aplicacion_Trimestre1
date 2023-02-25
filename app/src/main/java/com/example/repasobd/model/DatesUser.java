package com.example.repasobd.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatesUser extends SQLiteOpenHelper {
    Context context;


    private static final int DB_VERSION = 3;
    //nombre base de datos
    private static final String TABLE_NAME = "bd_Usuario";

    //nombre de las columnas
    private static final String USER_COLUMN = "user";
    private static final String PASSWORD_COLUMN = "pass_user";
    //Database name
    private static final String DB_NAME = "bd_user";

    //Constructor de la Base de Datos

    public DatesUser(Context context1) {
        super(context1, DB_NAME, null, DB_VERSION);
        context = context1;

    }

    /**
     * Método para insertar un usuario
     * @param user el nombre del usuario
     * @param password la contraseña insertada
     * @return numFilas
     */
    public long insertarUsuario(String user, String password){
        SQLiteDatabase baseDatossql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_COLUMN,user);
        cv.put(PASSWORD_COLUMN,password);
        long numFilas = -1;
        numFilas = baseDatossql.insert(TABLE_NAME , null ,cv );
        baseDatossql.close();
        return numFilas;
    }
    //Método sirve para chequear el usuario
    public boolean checkUser (String user){
        boolean devuelveValor = false;
        //Escribe en la base de datos
        SQLiteDatabase baseDatossql = this.getWritableDatabase();
        //Cursor recorre la sentencia SELECt
        Cursor cursor = baseDatossql.rawQuery(" SELECT * FROM "+TABLE_NAME + " WHERE "
                +USER_COLUMN + " = '" + user + "'", null);
        //Cursor esta en la primera poscion devuelve true
        if (cursor.moveToFirst()) {
            devuelveValor = true;
        }
        //Cierra el cursor, el base de datos
        cursor.close();
        baseDatossql.close();
        //Retorna el valor al habero chequeado
        return devuelveValor;
    }
    //Método sirve para chequear la contraseña
    public boolean checkPassword(String username, String password){
        //Variable booleana para comprobar que la contraseña existe
        boolean existe = false;
        //Le la baseDatos
        SQLiteDatabase MyDB = this.getReadableDatabase();
        try{
            //Recorre la sentencia SELECT
            Cursor cursor = MyDB.rawQuery("SELECT * FROM " + TABLE_NAME +
                    " WHERE " + USER_COLUMN + " = '" + username +
                    "' AND " + PASSWORD_COLUMN + " = '" + password + "'", null);
            //Cursor esta en la primera poscion devuelve true
            if (cursor.moveToFirst()) {
                existe = true;
            }
            //Cierra el cursor
            cursor.close();
        }catch(SQLException ex){}
        //Cierra la base de datos
        MyDB.close();
        return existe;
    }
    //Sobrecargamos onCreate
    // Encarga de crear las tablas asociadas a la base de datos.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //comando de la tabla
        String CREATE_USER_TABLE = "CREATE TABLE " +TABLE_NAME+ "("
                + USER_COLUMN + " TEXT PRIMARY KEY, " + PASSWORD_COLUMN + "  TEXT) ";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int datoAntiguo, int datoNuevo) {
        switch (datoAntiguo) {
            case 1:
                Log.i("DB", "BBDD Actualizada a la versión 2");
            case 2:
                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + USER_COLUMN + " TEXT" + PASSWORD_COLUMN + "TEXT");
                Log.i("DB", "BBDD Actualizada a la versión 3");
        }


    }
}
