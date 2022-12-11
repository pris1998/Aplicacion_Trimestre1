package com.example.repasobd.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public long insertarUsuario(ClassUser user){
        //metodo de buscar el usuario y si esta vacio
        SQLiteDatabase baseDatossql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_COLUMN,user.getUsuario());
        cv.put(PASSWORD_COLUMN,user.getContrasenia());
        long numFilas = baseDatossql.insert(TABLE_NAME , null ,cv );

        return numFilas;
    }
    /**
     * Este metodo de buscar el UsuarioClass me va a buscar el usuario registrado
     *
     * */

    public boolean  selecionarUsuarios(String...params){

        boolean esValido = true;
        listaUsuarios.clear();
        String[] columns = new String[]{USER_COLUMN, PASSWORD_COLUMN};
        String selection = "" ;
        String[] filter = null;
        baseDatossql = getReadableDatabase();
        switch(params.length){
            case 1:
                selection = USER_COLUMN + "=? AND" + PASSWORD_COLUMN +"=?";
                filter = new String[]{params[0] , params[1]};
                break;
        }
        Cursor cursor =baseDatossql.query(TABLE_NAME , columns ,selection,filter, null, null, null);


        //preguntamos si esta vacio
        if (cursor!=null && cursor.moveToFirst()){
            //recorrer nuestro resultado
            do{
                //si nos devuelve algun registro
                user = new ClassUser();
                user.setIdUsuario(cursor.getInt(0));
                user.setUsuario(cursor.getString(1));
                user.setContraseña(cursor.getString(2));
                user.setEmailUser(cursor.getString(3));
                //añadir lo anterior a la lista de los UsuarioClass
                listaUsuarios.add(user);
            }while(cursor.moveToNext());
        }
        return esValido;
    }
    //busque dentro de la base de datos el usuario
    public int loginMetodo(String usuario, String pass) {
        int devuelveValor = -1;
        String selection = "";
        String[] columns = {USER_COLUMN};

        baseDatossql = getReadableDatabase();

        selection = USER_COLUMN + "= ? AND " + PASSWORD_COLUMN + "= ?";
        String[]filter = {usuario, pass};


        Cursor cursor = baseDatossql.query(TABLE_NAME, columns, selection, filter, null, null, null);

        //preguntamos si no  esta vacio
        if ( cursor.getCount() > 0 ) {
            //recorrer nuestro resultado

            //me duvuelva el resultado de los dos valores del usuario
            devuelveValor = 1;
            cursor.close();

        }
        baseDatossql.close();

        return devuelveValor;

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
