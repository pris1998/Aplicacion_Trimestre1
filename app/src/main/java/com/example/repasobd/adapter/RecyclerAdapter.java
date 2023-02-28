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
    public int pos;
    //Creacion de los sensores
    private View.OnLongClickListener longClicklistener;
    private View.OnClickListener clickListener;
    public boolean isSelected;
    //ArrayList almacena los personajes de la API de ammibo
    ArrayList<Amiibo> listaAmiibo;

    public RecyclerAdapter(List<Amiibo> listaAmiibo) {
        this.listaAmiibo = (ArrayList<Amiibo>) listaAmiibo;
        listaAmiibo = new ArrayList<>();
        this.listaAmiibo.addAll(listaAmiibo);
    }
    //Creacion de los metodos de listeners
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
    //Creación de la estructura de componentes de cada celta de la lista
    @NonNull
    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_list,parent, false);

        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        return recyclerHolder;
    }

    //Enlaza y crea la vista con la informacion de cada una de las celdas
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerHolder holder, int position) {
        CircularProgressDrawable circularprogressdrawable;
        Amiibo amiibo = listaAmiibo.get(position);
        //Se configura el CircularProgressDrawable
        circularprogressdrawable = new CircularProgressDrawable(holder.itemView.getContext());
        circularprogressdrawable.setStrokeWidth(10f);
        circularprogressdrawable.setStyle(CircularProgressDrawable.LARGE);
        circularprogressdrawable.setCenterRadius(30f);
        circularprogressdrawable.start();

        holder.txtAmiiboSerie.setText(amiibo.getAmiiboSeries());
        holder.txtCharacter.setText(amiibo.getCharacter());
        holder.txtGameS.setText(amiibo.getGameSeries());
        //Glide para la image
        Glide.with(holder.itemView.getContext())
                .load(amiibo.getImagen())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imagen);



    }
    //Método para filtar la lista
    public void filtrado(String cadenaTexto){
        int longitud = cadenaTexto.length();
        if (longitud == 0) {
            //Limpia la lista
            listaAmiibo.clear();
            //Añade a la lista un nuevo elemento
            listaAmiibo.addAll(listaAmiibo);
        }else{

            List<Amiibo> collectionNew = listaAmiibo.stream()
                    .filter(i -> i.getCharacter().toLowerCase()
                    .contains(cadenaTexto.toLowerCase()))
                    .collect(Collectors.toList());
            listaAmiibo.clear();
            listaAmiibo.addAll(listaAmiibo);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {return listaAmiibo.size();}

    // Recargar elementos de la vista ,cada uno de los elementos de la lista con su modelode dato.
    public class RecyclerHolder extends RecyclerView.ViewHolder{
        TextView txtAmiiboSerie;
        TextView txtCharacter;
        TextView txtGameS;
        ImageView imagen;

        // Recoge el tipo de vista con los parametros
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
