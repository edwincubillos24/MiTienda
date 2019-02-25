package com.cubidevs.mitienda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    byte[] data;
    private EditText eNombre, eDescripcion, ePrecio;
    private String urlFoto = "";
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
        final DatabaseReference myRef = database.getReference("productos");

        final String nombre = eNombre.getText().toString();
        final String descripcion = eDescripcion.getText().toString();
        final int precio = Integer.parseInt(ePrecio.getText().toString());
        final String id = myRef.push().getKey();

        if (nombre.equals("") ||
                descripcion.equals("") ||
                ePrecio.getText().toString().equals("")) {
            Toast.makeText(ProductoNuevoActivity.this, "Debe digitar todos los campos", Toast.LENGTH_LONG).show();
        } else {

            mStorageRef = FirebaseStorage.getInstance().getReference();

            ByteArrayOutputStream baos = new ByteArrayOutputStream(); //comprimir
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            final StorageReference ref = mStorageRef.child("producto/" + id + ".jpg");
            UploadTask uploadTask = ref.putBytes(data);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        urlFoto = downloadUri.toString();

                        Producto producto = new Producto(id,
                                nombre,
                                descripcion,
                                urlFoto,
                                precio);

                        myRef.child(producto.getId()).setValue(producto);
                        onBackPressed();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }

    public void onFotoClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "ERROR Cargando Foto", Toast.LENGTH_SHORT).show();
            } else {
                Uri imagen = data.getData();

                try {
                    InputStream is = getContentResolver().openInputStream(imagen);
                    BufferedInputStream bis = new BufferedInputStream((is));
                    bitmap = BitmapFactory.decodeStream(bis);
                    iFoto.setImageBitmap(bitmap);
                    // guardarImagen(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
