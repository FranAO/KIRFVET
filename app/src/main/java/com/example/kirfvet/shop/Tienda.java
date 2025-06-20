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
import com.example.kirfvet.main.MainActivity;
import com.example.kirfvet.pets.CalendarioCitas;
import com.example.kirfvet.utils.InfoUsuario;
import com.example.kirfvet.AgendarCitas;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Tienda extends AppCompatActivity {

    private GridView gridViewMenu;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tienda);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0); // No aplicar padding inferior al contenedor principal
            return insets;
        });

        // Ajustar padding del menú de navegación inferior para que se pegue al borde real de la pantalla
        View bottomNav = findViewById(R.id.bottomNavigation);
        ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(0, 0, 0, bottomInset);
            return insets;
        });

        // Inicializar GridView y BottomNavigationView
        gridViewMenu = findViewById(R.id.gridViewMenu);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Marcar el ítem del carrito como seleccionado
        bottomNavigation.setSelectedItemId(R.id.navigation_cart);

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

        // Configurar listener para el menú de navegación
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                return true;
            } else if (itemId == R.id.navigation_calendar) {
                Intent intent = new Intent(this, CalendarioCitas.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_user) {
                Intent intent = new Intent(this, InfoUsuario.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_add) {
                Intent intent = new Intent(this, AgendarCitas.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Asegurarse de que el ítem del 'carrito' esté seleccionado al volver a esta actividad
        bottomNavigation.setSelectedItemId(R.id.navigation_cart);
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