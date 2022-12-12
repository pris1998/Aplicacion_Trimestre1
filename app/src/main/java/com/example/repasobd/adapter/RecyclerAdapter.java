package com.example.repasobd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.repasobd.R;
import com.example.repasobd.controller.MainActivity;
import com.example.repasobd.model.Anime;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{
    List<Anime> listMovies;

    public RecyclerAdapter(List<Anime> listMovies) {
        this.listMovies = listMovies;
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
        Anime anime = listMovies.get(position);
        circularprogressdrawable = new CircularProgressDrawable(holder.itemView.getContext());
        circularprogressdrawable.setStrokeWidth(10f);
        circularprogressdrawable.setStyle(CircularProgressDrawable.LARGE);
        circularprogressdrawable.setCenterRadius(30f);
        circularprogressdrawable.start();
        /**
         * Esta parte es la del ImfenView crea un Glide llamando desde un enlace a la foto
         */
        Glide.with(holder.itemView.getContext())
                .load(anime.getUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.img);

        holder.txtViewArtista.setText(anime.getAnime_name());

       
    }

    @Override
    public int getItemCount() {return listMovies.size();}

    /**
     * Esta clase es la que hereda de ViewHolder.Se encarga de recargar lso elementos
     * de la vista de layout de cada elemento de la lista según el modelo de datos
     * que recibe con custom_item_list.xml
     */
    public class RecyclerHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txtViewArtista;
        TextView txtViewUrlArtista;

        /**
         * Este es el contructor que recibe como parametro el tipo de viste ,
         * contiene los parametros que corresponde a los elementos del custom_item_list.xml
         * @param itemView
         */
        public RecyclerHolder(View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.img_item);
            txtViewArtista = itemView.findViewById(R.id.textViewTitulo);
            txtViewUrlArtista = itemView.findViewById(R.id.textViewDescripcion);
        }
    }
}
