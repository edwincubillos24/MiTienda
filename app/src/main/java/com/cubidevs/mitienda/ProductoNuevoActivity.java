package com.cubidevs.mitienda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProductoNuevoActivity extends AppCompatActivity {

    private EditText eNombre, eDescripcion, ePrecio;
    private String urlFoto="";
    private Bitmap bitmap;
    private ImageView iFoto;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_nuevo);

        eNombre = findViewById(R.id.eNombreProducto);
        eDescripcion = findViewById(R.id.eDescripcion);
        ePrecio = findViewById(R.id.ePrecio);
        iFoto = findViewById(R.id.iFoto);
    }

    public void onGuardarProductoClicked(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("productos");

        String nombre = eNombre.getText().toString();
        String descripcion = eDescripcion.getText().toString();
        int precio = Integer.parseInt(ePrecio.getText().toString());

        if (nombre.equals("") ||
                descripcion.equals("") ||
                ePrecio.getText().toString().equals("")){
            Toast.makeText(ProductoNuevoActivity.this, "Debe digitar todos los campos", Toast.LENGTH_LONG).show();
        } else {
            Producto producto = new Producto (myRef.push().getKey(),
                    nombre,
                    descripcion,
                    urlFoto,
                    precio);

            myRef.child(producto.getId()).setValue(producto);
            onBackPressed();
        }
    }

    public void onFotoClicked(View view) {
        Intent intent = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK){
            if (data == null){
                Toast.makeText(this, "ERROR Cargando Foto", Toast.LENGTH_SHORT).show();
            } else {
                Uri imagen = data.getData();

                try {
                    InputStream is = getContentResolver().openInputStream(imagen);
                    BufferedInputStream bis = new BufferedInputStream((is));
                    bitmap = BitmapFactory.decodeStream(bis);
                    iFoto.setImageBitmap(bitmap);
                    guardarImagen(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void guardarImagen(Bitmap bitmap) {
        mStorageRef = FirebaseStorage.getInstance().getReference();

        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //comprimir
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte [] data = baos.toByteArray();

        mStorageRef.child("productos").child("imagen1.jpeg").putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        urlFoto = mStorageRef.getDownloadUrl().toString();
                        Log.d("urlFoto",urlFoto);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ProductoNuevoActivity.this,"ERROR Subiendo Foto",
                                Toast.LENGTH_LONG).show();
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }


}
