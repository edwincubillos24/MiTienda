package com.cubidevs.mitienda;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder>{

    private ArrayList<Producto> productosList;
    private int resource;

    public ProductosAdapter(ArrayList<Producto> productosList, int resource) {
        this.productosList = productosList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        return new ProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder productoViewHolder, int i) {
        Producto producto = productosList.get(i);
        productoViewHolder.bindProducto(producto);
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }


    public class ProductoViewHolder extends RecyclerView.ViewHolder{

        private TextView tNombre, tDescripcion, tPrecio;
        private ImageView iFoto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tNombre = itemView.findViewById(R.id.tNombre);
            tPrecio = itemView.findViewById(R.id.tPrecio);
            tDescripcion = itemView.findViewById(R.id.tDescripcion);
            iFoto = itemView.findViewById(R.id.iFoto);
        }

        public void bindProducto(Producto producto){
            tNombre.setText(producto.getNombre());
            tDescripcion.setText(producto.getDescripcion());
            tPrecio.setText(String.valueOf(producto.getPrecio()));
            Picasso.get().load(producto.getImagen()).into(iFoto);
        }
    }
}
