package com.cubidevs.mitienda;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder>{

    private ArrayList<Producto> productosList;
    private int resource;
    private Activity activity;

    public ProductosAdapter(Activity activity, ArrayList<Producto> productosList, int resource) {
        this.productosList = productosList;
        this.resource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
        return new ProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder productoViewHolder, int i) {
        final Producto producto = productosList.get(i);
        productoViewHolder.bindProducto(producto);

        productoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, producto.getNombre(), Toast.LENGTH_SHORT).show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef =  database.getReference("productos");
                myRef.child(producto.getId()).removeValue();
            }
        });
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
            if (!(producto.getImagen().equals("")))
                Picasso.get().load(producto.getImagen()).into(iFoto);
        }
    }
}
