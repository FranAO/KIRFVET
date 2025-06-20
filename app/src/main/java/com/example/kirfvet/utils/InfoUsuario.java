package com.example.kirfvet.utils;

import android.os.Bundle;
import android.view.View;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kirfvet.R;
import com.example.kirfvet.animations.AnimatedLogoutButton;
import android.widget.ImageButton;

public class InfoUsuario extends AppCompatActivity {
    private AnimatedLogoutButton logoutButton;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_usuario);

        // Cambia el color de la status bar para que combine con el fondo blanco
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        // Cambia el color de la navigation bar para que combine con el fondo blanco
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));

        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar la funcionalidad que necesites cuando se presione el botón
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar explícitamente a MainActivity
                android.content.Intent intent = new android.content.Intent(InfoUsuario.this, com.example.kirfvet.main.MainActivity.class);
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }
}