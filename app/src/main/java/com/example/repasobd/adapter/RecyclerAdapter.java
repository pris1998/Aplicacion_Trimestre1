package com.example.repasobd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.repasobd.R;
import com.example.repasobd.model.Amiibo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    //implements View.OnClickListener
    List<Amiibo> listaAmiibo;
    public int pos;
    private View.OnLongClickListener longClicklistener;
    private View.OnClickListener clickListener;
    public boolean isSelected;
    //
    //lista parafiltrar en la Busqueda
    ArrayList<Amiibo> listaAux;

    public RecyclerAdapter(List<Amiibo> listaAmiibo) {
        this.listaAmiibo = listaAmiibo;
        listaAux = new ArrayList<>();
        this.listaAux.addAll(listaAmiibo);
    }

    public View.OnLongClickListener getLongClicklistener() {
        return longClicklistener;
    }

    public void setLongClicklistener(View.OnLongClickListener longClicklistener) {
        this.longClicklistener = longClicklistener;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * Este metodo crea la estructura de los elementos de cada celda , a partir del layout
     * Creo un objeto de la vista, coger la vista y anidarla en la estructura una vez hecho eso
     * retornamos el objeto creado del recyclerholder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_list,parent, false);
        //
        //view.setOnLongClickListener((View.OnLongClickListener) this);
        //
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        return recyclerHolder;
    }

    /**
     * Este metodo se encarga de unir la informacion con cada celda del RecyclerView
     * Crea un objeto de mi clase Peliculas para luego asignarle la lista donde almacena los elementos
     * una vex hecho eso, asigna a cada elemento la informacion en la lista y tambien
     * en cada elemento de la gráfica de la celda
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerHolder holder, int position) {
        CircularProgressDrawable circularprogressdrawable;
        Amiibo amiibo = listaAmiibo.get(position);
        circularprogressdrawable = new CircularProgressDrawable(holder.itemView.getContext());
        circularprogressdrawable.setStrokeWidth(10f);
        circularprogressdrawable.setStyle(CircularProgressDrawable.LARGE);
        circularprogressdrawable.setCenterRadius(30f);
        circularprogressdrawable.start();
        /**
         * Esta parte es la del ImfenView crea un Glide llamando desde un enlace a la foto
         */
        holder.txtAmiiboSerie.setText(amiibo.getAmiiboSeries());
        holder.txtCharacter.setText(amiibo.getCharacter());
        holder.txtGameS.setText(amiibo.getGameSeries());
        Glide.with(holder.itemView.getContext())
                .load(amiibo.getImagen())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imagen);



    }
    public void filtrado(String cadenaTexto){
        int longitud = cadenaTexto.length();
        if (longitud == 0) {
            listaAmiibo.clear();
            listaAmiibo.addAll(listaAux);
        }else{

            List<Amiibo> collectionNew = listaAmiibo.stream()
                    .filter(i -> i.getCharacter().toLowerCase()
                    .contains(cadenaTexto.toLowerCase()))
                    .collect(Collectors.toList());
            listaAmiibo.clear();
            listaAmiibo.addAll(listaAux);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {return listaAmiibo.size();}


    /*
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }*/
    //



    /**
     * Esta clase es la que hereda de ViewHolder.Se encarga de recargar lso elementos
     * de la vista de layout de cada elemento de la lista según el modelo de datos
     * que recibe con custom_item_list.xml
     */
    public class RecyclerHolder extends RecyclerView.ViewHolder{
        TextView txtAmiiboSerie;
        TextView txtCharacter;
        TextView txtGameS;
        ImageView imagen;


        /**
         * Este es el contructor que recibe como parametro el tipo de viste ,
         * contiene los parametros que corresponde a los elementos del custom_item_list.xml
         * @param itemView
         */
        public RecyclerHolder(View itemView){
            super(itemView);
            txtAmiiboSerie = itemView.findViewById(R.id.txtAmiiboSerie);
            txtCharacter = itemView.findViewById(R.id.txtCharacter);
            txtGameS = itemView.findViewById(R.id.txtGameS);
            imagen = itemView.findViewById(R.id.img_item);
            itemView.setOnLongClickListener(longClicklistener);
            itemView.setOnClickListener(clickListener);
        }


    }
}
