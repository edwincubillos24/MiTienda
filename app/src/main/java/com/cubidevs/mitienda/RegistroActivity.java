package com.cubidevs.mitienda;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {

    private EditText eCorreo, eContrasena, eRepContrasena;
    private Button bRegistrarse;
    private FirebaseAuth mAuth;
    private String TAG = "RegistroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        eCorreo = findViewById(R.id.eCorreo);
        eContrasena = findViewById(R.id.eContrasena);
        eRepContrasena = findViewById(R.id.eRepContrasena);
    }

    public void onRegistrarmeClicked(View view) {
        String correo = eCorreo.getText().toString();
        String contrasena = eContrasena.getText().toString();
        String repContrasena = eRepContrasena.getText().toString();

        mAuth = FirebaseAuth.getInstance();

        if (correo.equals("") || contrasena.equals("") || repContrasena.equals("")) {
            Toast.makeText(this, "Debe digitar todos los campos", Toast.LENGTH_LONG).show();
        } else if (contrasena.length() < 6) {
            Toast.makeText(this, "La contrasena debe tener mas de 6 caracteres", Toast.LENGTH_LONG).show();
        } else if (contrasena.equals(repContrasena)) {
            mAuth.createUserWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                onBackPressed();
                                //      updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistroActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //      updateUI(null);
                            }

                            // ...
                        }
                    });

        } else {
            Toast.makeText(this, "Contrasenas no son iguales", Toast.LENGTH_LONG).show();
        }
    }
}
