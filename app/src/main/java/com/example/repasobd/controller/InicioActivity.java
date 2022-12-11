package com.example.repasobd.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.repasobd.R;
import com.example.repasobd.adapter.RecyclerAdapter;
import com.example.repasobd.io.HttpConnectGeneral;
import com.example.repasobd.model.Anime;
import com.example.repasobd.utilities.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InicioActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    RecyclerView recyclerView;//uso para la API
    RecyclerAdapter recAdapter;
    private ArrayList<Anime> listaAnime = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //cargar Preferencias
        constraintLayout = findViewById(R.id.container_inicio);
        constraintLayout.setBackgroundColor(Preferences.loadPreferences(this));
        // incializo los objetos recyclerView y recyclerAdapter
        recyclerView = (RecyclerView) findViewById(R.id.recyView);
        //conexion API
        new taskConnections().execute("GET", "/hug?amount=2");

        // AletDialog sin boton , llamar al metodo
        AlertDialog alertDialog = createAlertDialog("Recordatorio", "Puede borrar elementos de la lista haciendo SWIPE "
                                                    + " tanto a la derecha como a la izquierda");
        alertDialog.show();
        /**
         * Flecha para volver atras
         */
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true); //si existe (no es nulo) mostramos el botón hacia atrás.
        }

        //BORRAR HACEINDO SWIPE
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder target, int direction) {
                int position = target.getAdapterPosition();
                listaAnime.remove(position);
                recAdapter.notifyDataSetChanged();
            }
        });
        helper.attachToRecyclerView(recyclerView);


    }

    /**
     * Manejo del MENU_SIMPLE
     * Metodo  indica que la app tiene un menu personalizado.
     * Uso el inflate para crear la vista y pasar el menu por defecto para así colocarlo en la vista
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }
    //FLECHA CONFIGURACION
    /*
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:  
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    /**
     * Metodo para manejar la seleciona del menu según el item seleccionado
     * @param item
     * @return true
     */
    public boolean onOptionsItemSelected(MenuItem item){
        int itemSeleccionado = item.getItemId();

        switch (itemSeleccionado){
            case R.id.item_Preferencias:
                //Boton que te dirige a la Actividad de Prefenrencias
                Intent intent = new Intent(InicioActivity.this , SettingActivity.class);
                startActivity(intent);
                myToast("Boton Preferencias seleccionado");
                Log.i("Hoja se preferencias ","Esta en la hoja de Prefencias , Conseguido");
                break;
            case R.id.item_Anadir:
                //Este boton no funciona al pulsar sobre el y que me lleve a la AddActivity
                //Boton que te dirige a la Actividad Nueva para añadir un elemento a la lista
                Intent intent2 = new Intent(InicioActivity.this , AddActivity.class);
                startActivity(intent2);
                myToast("Boton Añadir seleccionado");
                Log.i("Hoja se Añadir ","Esta en la hoja de Añadir , Conseguido");

                break;
        }
        return true;
    }
    public void myToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * RecyclerView
     */

    //agregar un elemento desde AddActivity al recyclerView
    public void agregarPersonaje (Anime anime){
        listaAnime.add(anime);
        // this.notifyItemInserted(this.mascotas.size() - 1);
    }

    /**
     * Carga de OnResume
     */
    @Override
    protected void onResume() {
        super.onResume();
        constraintLayout.setBackgroundColor(Preferences.loadPreferences(this));
    }

    /**
     * Configuracion de la API
     */
    private class taskConnections extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String salida = null;
            switch (strings[0]){
                case "GET":
                    salida = HttpConnectGeneral.getRequest(strings[1]);
                    break;
                 }
            return salida;
        }
        @Override
        protected void onPostExecute(String texto){
        try{
            if (texto != null) {
                Log.d("D","Datos recibidos "+texto);
               JSONObject jsonObject = new JSONObject(texto);
               JSONArray jsonArrayName = jsonObject.getJSONArray("results");
               JSONArray jsonArrayUrl = jsonObject.getJSONArray("results");


                String anime_name = "";
                String url = "";


                for (int i = 0; i < jsonArrayName.length(); i++) {
                    //revisar el JSON
                    anime_name = jsonArrayName.getJSONObject(i).getString("anime_name");
                    url = jsonArrayUrl.getJSONObject(i).getString("url");

                    listaAnime.add(new Anime(anime_name,url));
                }
                mostrarDatos();

            }else{
                Toast.makeText(InicioActivity.this, "Error:Al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    }

    private void mostrarDatos(){
        /**
         * Disponer del espacio que se usara para colocar los elementos en su lugar
         * Añadimos los elementos creados a la vista del RecyclerView con los métodos
         */
        recAdapter = new RecyclerAdapter(listaAnime);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Metodo para crear el AlertDialog y sus botones
      * @param nameAlert
     * @param mensaje
     * @return
     */
    public AlertDialog createAlertDialog (String nameAlert, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(InicioActivity.this);
        builder.setMessage(mensaje).setTitle(nameAlert);
        //este codigo es una vez que estas en la pantalla modal del AlertDialog
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(InicioActivity.this, "Ha pulsado el botón de Si",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(InicioActivity.this, "Ha pulsado el botón de No",Toast.LENGTH_LONG).show();
            }
        });
        return builder.create();
    }






}