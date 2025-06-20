package com.example.kirfvet.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kirfvet.R;

public class InicioSesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_sesion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView forgotPassword = findViewById(R.id.textViewForgotPassword);
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, Contrasena.class);
            startActivity(intent);
        });

        TextView signUp = findViewById(R.id.textViewSignUpLink);
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, Registro.class);
            startActivity(intent);
        });
    }
}