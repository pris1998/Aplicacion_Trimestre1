package com.example.repasobd.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.repasobd.R;
import com.example.repasobd.adapter.RecyclerAdapter;
import com.example.repasobd.io.HttpConnectGeneral;
import com.example.repasobd.model.Amiibo;
import com.example.repasobd.utilities.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InicioActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ConstraintLayout constraintLayout;
    //variable RecyclerView
    RecyclerView recyclerView;//uso para la API
    //variable del RecyclerAdapter
    public RecyclerAdapter recAdapter;
    //variable del progreso
    TextView txtProgreso;
    private int amiiboSeleccionado = 0;

    private ArrayList<Amiibo> listaAmiibo = new ArrayList<>();
    private androidx.appcompat.view.ActionMode mActionMode;
    private androidx.appcompat.view.ActionMode mActionModeAmiibo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //cargar Preferencias
        constraintLayout = findViewById(R.id.container_inicio);
        constraintLayout.setBackgroundColor(Preferences.loadPreferences(this));
        // incializo los objetos recyclerView y recyclerAdapter
        recyclerView = (RecyclerView) findViewById(R.id.recyView);
        //inicializo la variable del progreso
        txtProgreso = (TextView) findViewById(R.id.txtProgreso);
        //llamada al menu_action
        mActionMode = startSupportActionMode(mActionCallback);
        //se carga la pagina hasta salid toda la informacion requerida
        new publishTask().execute();

        //Activacion de la flecha para volver hacia atrás
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true); //si existe (no es nulo) mostramos el botón hacia atrás.
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        recAdapter.filtrado(s);
        return false;
    }


    //Hilo secundario para cargar la informacion de la API

    private class publishTask extends AsyncTask<Void, Integer, Void>{
        @Override
        protected Void doInBackground(Void... voids) {

            for(int i=0; i<=100 ; i++){
                try {
                    Thread.sleep(50);
                    //llamada al onProgressUpdate() pasandole como parametro el
                    // elemento para actualizar la vista
                    publishProgress(i);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //le asignamos lo que va a sacer el txtProgreso por pantalla
            txtProgreso.setText(values[0].toString() + "%");
        }

        @Override
        protected void onPostExecute(Void unused) {
            //Posibilitamos el txtProgreso
            txtProgreso.setEnabled(false);
            //llamamos a la API para que haga una especie de recarga hasta que aparaezcan
            //todos los elementos
            new taskConnections().execute("GET", "amiibo/?name=mario");
            //hacemos el txtProgreso invisible cuando se haya cargado la lista
            txtProgreso.setVisibility(View.INVISIBLE);
        }
    }


    //LLamamos al menu y le añadimos los métodos
    private ActionMode.Callback mActionCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_menu,menu);
            mode.setTitle("Menu del Amiibo");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemSeleccionado = item.getItemId();
            switch (itemSeleccionado) {
                case R.id.item_Preferencias:
                    //Intent que dirije hacia la actividad de Prefernecias
                    Intent intent = new Intent(InicioActivity.this, SettingActivity.class);
                    startActivity(intent);
                    myToast("Boton Preferencias seleccionado");
                    break;
                case android.R.id.home:
                    onBackPressed();
                    return true;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    private ActionMode.Callback mActionAmiiboCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //Creacion del menu para borrar elementos de la lista
            mode.getMenuInflater().inflate(R.menu.action_menu_amiibo,menu);
            mode.setTitle("LISTA");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemSeleccionado = item.getItemId();
            switch (itemSeleccionado) {
                case R.id.item_eliminar:
                    AlertDialog alertDialog = createAlertDialog("ALERTA","¿Seguro que quieres borrar el elemento?");
                    alertDialog.show();
                    mActionModeAmiibo.finish();
                    break;
                case android.R.id.home:
                    onBackPressed();
                    return true;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionModeAmiibo = null;
        }
    };


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    public void myToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * Mantiene todo guardado desde el último momento que se tocó
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
        //Llama desde segundo plano para obtener la informacion de la API
        @Override
        protected String doInBackground(String... strings) {
            String salida = null;
            switch (strings[0]){
                case "GET":
                    salida = HttpConnectGeneral.getRequest("");
                    break;
                 }
            return salida;
        }
        @Override
        protected void onPostExecute(String texto){
        try{
            if (texto != null) {

                String character = "";
                String amiiboSeries = "";
                String gameSeries = "";
                String imagen = "";
                //Almacena los datos de la API en un archivo JSON
                Log.d("D","Datos recibidos "+texto);
                JSONObject raiz = new JSONObject(texto);
                JSONArray jsonArray = raiz.getJSONArray("amiibo");
                //Recorre el json
                for (int i = 0; i < jsonArray.length(); i++) {
                    //va sacando la informacion , en este
                    //caso el personaje, la imagen...
                    JSONObject amiiboJSON = jsonArray.getJSONObject(i);
                    character = amiiboJSON.getString("character");
                    amiiboSeries = amiiboJSON.getString("amiiboSeries");
                    gameSeries = amiiboJSON.getString("gameSeries");
                    imagen = amiiboJSON.getString("image");
                    //añade la informacion a la lista
                    listaAmiibo.add(new Amiibo(character,amiiboSeries,gameSeries,imagen));
                }
                //llamamos al metodo de mostrar los datos
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
        //Colocacion de los elementos en el lugar que corresponde y añade esos
        //elementos a la vista creada del RecyclerView

        recAdapter = new RecyclerAdapter(listaAmiibo);
        recAdapter.setLongClicklistener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //A la variable amiiboSelecionado inicializamos los elementos de la lista
                //que va seleccionando
                amiiboSeleccionado = recyclerView.getChildAdapterPosition(view);
                Toast.makeText(view.getContext(),"Has tocado el elemento " + amiiboSeleccionado,Toast.LENGTH_SHORT).show();
                mActionModeAmiibo = startSupportActionMode(mActionAmiiboCallback);
                return true;
            }
        });
        recAdapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amiiboSeleccionado = recyclerView.getChildAdapterPosition(view);
                //Obtener el amibo seleccionado de la lista
                Amiibo amiibo = listaAmiibo.get(amiiboSeleccionado);
                //Cambia de vista
                Intent intent = new Intent(InicioActivity.this, AddActivity.class);
                //añade el valor del EditText al intent
                intent.putExtra("AMIIBO", amiibo);
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
    //Cracion de un metodo del AlertDialog
    public AlertDialog createAlertDialog (String nameAlert, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(mensaje).setTitle(nameAlert);
        //Si pulsa "si" borra el elemento de la lista
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                borrar(recAdapter.pos,1);
            }
        });
        //Si pulsa "no"el elemento de la lsita se mantiene
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(InicioActivity.this, "Ha seleccionado No",Toast.LENGTH_LONG).show();
            }
        });
        return builder.create();
    }
    //Elemento para borrar elementos
    public void borrar(int position, int borrar){
        if(borrar == 1 ){
            listaAmiibo.remove(amiiboSeleccionado);
            recAdapter.notifyItemRemoved(amiiboSeleccionado);
        }

    }
    //metodo que hace referencia a la flecha
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:  //  acceso al recurso del botón volver
                onBackPressed();     // vuelve hacia la ventana anterior.
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



}

