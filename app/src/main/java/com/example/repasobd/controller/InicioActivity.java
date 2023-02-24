package com.example.repasobd.controller;

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
    RecyclerView recyclerView;//uso para la API
    public RecyclerAdapter recAdapter;
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

        txtProgreso = (TextView) findViewById(R.id.txtProgreso);
        //el menu action llamada
        mActionMode = startSupportActionMode(mActionCallback);
        //cargue la pagina hasta que salga toda la informacion
        new publishTask().execute();



        /**
         * Flecha para volver atras
         */
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


    //Todo.Cargar la informacion de la API ponemos un hilo secunadario

    private class publishTask extends AsyncTask<Void, Integer, Void>{
        @Override
        protected Void doInBackground(Void... voids) {

            for(int i=0; i<=100 ; i++){
                try {
                    Thread.sleep(50);
                    //Todo 1.3. Método que llama a onProgressUpdate() pasandole como parametro el
                    // elemento necesario para actualizar la vista
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
            txtProgreso.setText(values[0].toString() + "%");
        }

        @Override
        protected void onPostExecute(Void unused) {
            txtProgreso.setEnabled(false);
            new taskConnections().execute("GET", "amiibo/?name=mario");
            txtProgreso.setVisibility(View.INVISIBLE);
        }
    }


    //Menu action
    private ActionMode.Callback mActionCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_menu,menu);
            mode.setTitle("Action Menu");
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
                    //Boton que te dirige a la Actividad de Prefenrencias
                    Intent intent = new Intent(InicioActivity.this, SettingActivity.class);
                    startActivity(intent);
                    myToast("Boton Preferencias seleccionado");
                    Log.i("Hoja se preferencias ", "Esta en la hoja de Prefencias , Conseguido");
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

                Log.d("D","Datos recibidos "+texto);
                JSONObject raiz = new JSONObject(texto);
                JSONArray jsonArray = raiz.getJSONArray("amiibo");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject amiiboJSON = jsonArray.getJSONObject(i);
                    //revisar el JSON
                    character = amiiboJSON.getString("character");
                    amiiboSeries = amiiboJSON.getString("amiiboSeries");
                    gameSeries = amiiboJSON.getString("gameSeries");
                    imagen = amiiboJSON.getString("image");

                    listaAmiibo.add(new Amiibo(character,amiiboSeries,gameSeries,imagen));
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
        recAdapter = new RecyclerAdapter(listaAmiibo);
        recAdapter.setLongClicklistener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                amiiboSeleccionado = recyclerView.getChildAdapterPosition(view);
                Toast.makeText(view.getContext(),"Has tocado el elemento " + amiiboSeleccionado,Toast.LENGTH_SHORT).show();
                mActionModeAmiibo = startSupportActionMode(mActionAmiiboCallback);
                //createAlertDialog("Borrado","¿Estás seguro de borrar ?");
                return true;
            }
        });
        recAdapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amiiboSeleccionado = recyclerView.getChildAdapterPosition(view);
                Log.d("Hola", "wenos dias");
                Amiibo amiibo = listaAmiibo.get(amiiboSeleccionado);
                Intent intent = new Intent(InicioActivity.this, AddActivity.class);
                intent.putExtra("AMIIBO", amiibo);
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(recAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public AlertDialog createAlertDialog (String nameAlert, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(mensaje).setTitle(nameAlert);
        //este codigo es una vez que estas en la pantalla modal del AlertDialog
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                borrar(recAdapter.pos,1);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(InicioActivity.this, "Ha seleccionado No",Toast.LENGTH_LONG).show();
            }
        });
        return builder.create();
    }
    public void borrar(int position, int borrar){

        if(borrar == 1 ){
            listaAmiibo.remove(amiiboSeleccionado);
            recAdapter.notifyItemRemoved(amiiboSeleccionado);
        }

    }



}

