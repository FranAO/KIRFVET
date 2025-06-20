package com.example.kirfvet.main;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kirfvet.R;
import com.example.kirfvet.pets.CalendarioCitas;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {
    // Componentes de la interfaz
    private MaterialCardView searchBar;
    private EditText searchEditText;
    private ImageView searchIcon;
    private BottomNavigationView bottomNavigation;
    
    // Variables de control
    private boolean isExpanded = false;
    private ValueAnimator animator;
    private boolean isHovering = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        searchBar = findViewById(R.id.searchBar);
        searchEditText = findViewById(R.id.searchEditText);
        searchIcon = findViewById(R.id.searchIcon);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Configurar ancho inicial
        ViewGroup.LayoutParams params = searchBar.getLayoutParams();
        params.width = (int) getResources().getDimension(R.dimen.search_collapsed_width);
        searchBar.setLayoutParams(params);

        // Configurar eventos táctiles
        searchBar.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isHovering = true;
                    if (!isExpanded) expandirBusqueda();
                    return true;
                    
                case MotionEvent.ACTION_MOVE:
                    // Verificar si el toque está dentro del área
                    if (estaDentroDelArea(event.getX(), event.getY(), v)) {
                        if (!isHovering && !isExpanded) {
                            isHovering = true;
                            expandirBusqueda();
                        }
                    } else if (isHovering && isExpanded) {
                        isHovering = false;
                        contraerBusqueda();
                    }
                    return true;
                    
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isHovering = false;
                    if (isExpanded) contraerBusqueda();
                    return true;
            }
            return false;
        });

        // Configurar click fuera del search bar
        findViewById(R.id.main).setOnClickListener(v -> {
            if (isExpanded) contraerBusqueda();
        });

        // Evitar que el click en el search bar se propague
        searchBar.setOnClickListener(v -> {});

        // Configurar listener para el menú de navegación
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_calendar) {
                Intent intent = new Intent(this, com.example.kirfvet.pets.CalendarioCitas.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_home) {
                // Ya estamos en home, solo permite la selección.
                return true;
            } else if (itemId == R.id.navigation_user) {
                Intent intent = new Intent(this, com.example.kirfvet.utils.InfoUsuario.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                Intent intent = new Intent(this, com.example.kirfvet.shop.Tienda.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_add) {
                Intent intent = new Intent(this, com.example.kirfvet.AgendarCitas.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Configurar insets del sistema SOLO lateral y superior, NO inferior
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Ajustar padding del menú de navegación inferior para que se pegue al borde real de la pantalla
        View bottomNav = findViewById(R.id.bottomNavigation);
        ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(0, 0, 0, bottomInset);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Asegurarse de que el ítem de 'home' esté seleccionado al volver a esta actividad
        bottomNavigation.setSelectedItemId(R.id.navigation_home);
    }

    // Verifica si las coordenadas están dentro del área del view
    private boolean estaDentroDelArea(float x, float y, View v) {
        return x >= 0 && x <= v.getWidth() && y >= 0 && y <= v.getHeight();
    }

    // Expande la barra de búsqueda
    private void expandirBusqueda() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        int anchoInicial = searchBar.getWidth();
        int anchoFinal = (int) getResources().getDimension(R.dimen.search_expanded_width);

        animator = ValueAnimator.ofInt(anchoInicial, anchoFinal);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        
        animator.addUpdateListener(animation -> {
            int valor = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = searchBar.getLayoutParams();
            params.width = valor;
            searchBar.setLayoutParams(params);
            
            // Animar la opacidad del texto
            float progreso = (float) (valor - anchoInicial) / (anchoFinal - anchoInicial);
            searchEditText.setAlpha(progreso);
            if (progreso > 0) searchEditText.setVisibility(View.VISIBLE);
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                isExpanded = true;
                searchEditText.setVisibility(View.VISIBLE);
                searchEditText.setAlpha(1f);
                searchEditText.requestFocus();
            }
        });

        animator.start();
    }

    // Contrae la barra de búsqueda
    private void contraerBusqueda() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        int anchoInicial = searchBar.getWidth();
        int anchoFinal = (int) getResources().getDimension(R.dimen.search_collapsed_width);

        animator = ValueAnimator.ofInt(anchoInicial, anchoFinal);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        
        animator.addUpdateListener(animation -> {
            int valor = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = searchBar.getLayoutParams();
            params.width = valor;
            searchBar.setLayoutParams(params);
            
            // Animar la opacidad del texto
            float progreso = (float) (valor - anchoFinal) / (anchoInicial - anchoFinal);
            searchEditText.setAlpha(progreso);
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                isExpanded = false;
                searchEditText.setVisibility(View.GONE);
                searchEditText.setAlpha(0f);
                searchEditText.clearFocus();
            }
        });

        animator.start();
    }
}