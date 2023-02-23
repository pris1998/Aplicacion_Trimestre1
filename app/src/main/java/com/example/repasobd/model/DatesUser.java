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
    ClassUser user;
    ArrayList<ClassUser> listaUsuarios;
    SQLiteDatabase baseDatossql ;

    private static final int DB_VERSION = 3;
    //nombre base de datos
    private static final String TABLE_NAME = "bd_Usuario";


    private static final String USER_COLUMN = "user";
    private static final String PASSWORD_COLUMN = "pass_user";
    //Database name
    private static final String DB_NAME = "bd_user";

    /**
     * Constructor de la base de datos, si no existe la base de datos la crea, sino se conecta.
     *  En el caso de que se hiciese una actualización y se cambiase la versión,
     *  el constructor llamaría al método onUpgrade para actualizar los cambios de la base de datos.
     * @param context1 Contexto de la aplicación
     */

    public DatesUser(Context context1) {
        super(context1, DB_NAME, null, DB_VERSION);
        context = context1;

    }

    public long insertarUsuario(String user, String password){
        //metodo de buscar el usuario y si esta vacio
        SQLiteDatabase baseDatossql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_COLUMN,user);
        cv.put(PASSWORD_COLUMN,password);
        long numFilas = -1;
        numFilas = baseDatossql.insert(TABLE_NAME , null ,cv );
        baseDatossql.close();
        return numFilas;
    }
    /**
     * Este metodo de buscar el UsuarioClass me va a buscar el usuario registrado
     *
     * */
    public boolean checkUser (String user){
        boolean devuelveValor = false;
        SQLiteDatabase baseDatossql = this.getWritableDatabase();
        Cursor cursor = baseDatossql.rawQuery(" SELECT * FROM "+TABLE_NAME + " WHERE "
                +USER_COLUMN + " = '" + user + "'", null);
        if (cursor.moveToFirst()) {
            devuelveValor = true;
        }
        cursor.close();
        baseDatossql.close();
        return devuelveValor;
    }

    public boolean checkPassword(String username, String password){
        boolean existe = false;
        SQLiteDatabase MyDB = this.getReadableDatabase();
        try{
            Cursor cursor = MyDB.rawQuery("SELECT * FROM " + TABLE_NAME +
                    " WHERE " + USER_COLUMN + " = '" + username +
                    "' AND " + PASSWORD_COLUMN + " = '" + password + "'", null);
            if (cursor.moveToFirst()) {
                existe = true;
            }
            cursor.close();
        }catch(SQLException ex){}
        MyDB.close();
        return existe;
    }
    //Sobrecargamos onCreate, encargado de crear las tablas asociadas a la base de datos.
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
