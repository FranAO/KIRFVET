package com.example.kirfvet.utils;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.view.WindowManager;
import android.os.Build;
import android.view.Window;
import androidx.core.content.ContextCompat;

import com.example.kirfvet.R;
import com.example.kirfvet.components.FloatingMenu;

public class Detalles extends AppCompatActivity {
    
    private FloatingMenu floatingMenu;
    private String petName = "Nombre de Mascota"; // Inicializa con un valor por defecto o cárgalo de intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        // Cambia el color de la status bar para que combine con el fondo
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_background));
        // Cambia el color de la navigation bar para que combine con el menú inferior (negro)
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        
        // Obtener referencia al FloatingMenu desde el layout
        floatingMenu = findViewById(R.id.floatingMenu);
        
        // Configurar el listener para las acciones
        floatingMenu.setOnMenuItemClickListener(new FloatingMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int itemId) {
                handleMenuItemClick(itemId);
            }
        });

        // Ejemplo de cómo podrías obtener el nombre de la mascota
        TextView tvPetName = findViewById(R.id.tvPetName);
        if (tvPetName != null) {
            petName = tvPetName.getText().toString();
        }
    }
    
    private void handleMenuItemClick(int itemId) {
        if (itemId == R.id.optionA) {
            // Editar mascota
            editPet();
        } else if (itemId == R.id.optionB) {
            // Compartir información
            sharePetInfo();
        } else if (itemId == R.id.optionC) {
            // Eliminar mascota
            deletePet();
        }
    }
    
    private void editPet() {
        // Implementar edición de mascota
        Toast.makeText(this, "Editar mascota", Toast.LENGTH_SHORT).show();
        // Intent para ir a pantalla de edición
    }
    
    private void sharePetInfo() {
        // Implementar compartir información
        Toast.makeText(this, "Compartir información", Toast.LENGTH_SHORT).show();
        
        // Ejemplo de compartir
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Información de mi mascota: " + petName);
        startActivity(Intent.createChooser(shareIntent, "Compartir información de mascota"));
    }
    
    private void deletePet() {
        // Mostrar diálogo de confirmación
        new AlertDialog.Builder(this)
            .setTitle("Eliminar mascota")
            .setMessage("¿Estás seguro de que quieres eliminar esta mascota?")
            .setPositiveButton("Eliminar", (dialog, which) -> {
                // Implementar eliminación
                Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
                finish();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
    
    @Override
    public void onBackPressed() {
        // Cerrar el menú si está abierto antes de hacer back
        if (floatingMenu != null && floatingMenu.isMenuOpen()) {
            floatingMenu.closeMenuIfOpen();
        } else {
            super.onBackPressed();
        }
    }
}