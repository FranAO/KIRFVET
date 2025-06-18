package com.example.kirfvet.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kirfvet.components.MenuAdapter;
import com.example.kirfvet.components.MenuItem;
import com.example.kirfvet.R;

import java.util.ArrayList;
import java.util.List;

public class Tienda extends AppCompatActivity {

    private GridView gridViewMenu;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tienda);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar GridView
        gridViewMenu = findViewById(R.id.gridViewMenu);

        // Crear elementos del menú
        initMenuItems();

        // Configurar adaptador con el layout item_tienda
        menuAdapter = new MenuAdapter(this, menuItems, R.layout.item_tienda);
        gridViewMenu.setAdapter(menuAdapter);

        // Configurar evento de clic
        gridViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleMenuSelection(position);
            }
        });
    }

    private void initMenuItems() {
        menuItems = new ArrayList<>();

        // Agregar opciones del menú con sus imágenes correspondientes
        menuItems.add(new MenuItem("Perro", R.drawable.dog));
        menuItems.add(new MenuItem("Gato", R.drawable.cat));
        menuItems.add(new MenuItem("Loro", R.drawable.parrot));
    }

    private void handleMenuSelection(int position) {
        Intent intent = null;

        switch (position) {
            case 0:
                intent = new Intent(this, TiendaPerro.class);
                break;
            case 1:
                intent = new Intent(this, TiendaGato.class);
                break;
            case 2:
                intent = new Intent(this, TiendaLoro.class);
                break;
            default:
                Toast.makeText(this, "Opción no implementada aún", Toast.LENGTH_SHORT).show();
                return;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}