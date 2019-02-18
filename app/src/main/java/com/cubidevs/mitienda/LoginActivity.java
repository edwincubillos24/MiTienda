package com.cubidevs.mitienda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView tRegistrarse;
    private EditText eCorreo,eContrasena;
    private FirebaseAuth mAuth;
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        tRegistrarse = findViewById(R.id.tRegistro);
        eCorreo = findViewById(R.id.eCorreo);
        eContrasena = findViewById(R.id.eContrasena);
    }

    public void onRegistrarseClicked(View view) {
        Intent intent = new Intent (this, RegistroActivity.class);
        startActivity(intent);

    }

    public void onIniciarSesionClicked(View view) {
        String correo = eCorreo.getText().toString();
        String contrasena = eContrasena.getText().toString();
        mAuth = FirebaseAuth.getInstance();

        if (correo.equals("") || contrasena.equals("")) {
            Toast.makeText(this, "Debe digitar todos los campos", Toast.LENGTH_LONG).show();
        } else if (contrasena.length() < 6) {
            Toast.makeText(this, "La contrasena debe tener mas de 6 caracteres", Toast.LENGTH_LONG).show();
        } else  {
            mAuth.signInWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Bienvenido.",
                                        Toast.LENGTH_SHORT).show();
                                goToProductosActivity();
                                //      updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //      updateUI(null);
                            }

                            // ...
                        }
                    });

        }




    }

    private void goToProductosActivity() {
        Intent intent = new Intent( LoginActivity.this, ProductosActivity.class);
        startActivity(intent);
        finish();
    }
}
