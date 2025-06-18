package com.example.kirfvet.components;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.kirfvet.R;

public class FloatingMenu extends FrameLayout implements View.OnClickListener {
    
    private ImageButton mainMenuButton;
    private ImageButton optionA, optionB, optionC;
    private boolean isMenuOpen = false;
    private OnMenuItemClickListener listener;
    
    // Distancias de animación
    private final int DISTANCE_X_A = -200; // Distancia horizontal para el botón superior (más a la derecha)
    private final int DISTANCE_X_B = -300; // Distancia horizontal para el botón medio (más a la izquierda)
    private final int DISTANCE_X_C = -200; // Distancia horizontal para el botón inferior (más a la derecha)
    private final int DISTANCE_Y_A = -150; // Distancia vertical para el botón superior
    private final int DISTANCE_Y_B = 0;    // Distancia vertical para el botón medio
    private final int DISTANCE_Y_C = 150;  // Distancia vertical para el botón inferior
    
    public interface OnMenuItemClickListener {
        void onMenuItemClick(int itemId);
    }
    
    public FloatingMenu(Context context) {
        super(context);
        init();
    }
    
    public FloatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        // Inflar el layout
        inflate(getContext(), R.layout.floating_menu, this);
        
        // Configurar el layout para que sea lo suficientemente grande
        setLayoutParams(new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ));
        
        // Obtener referencias a los botones
        mainMenuButton = findViewById(R.id.mainMenuButton);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        
        // Configurar listeners
        mainMenuButton.setOnClickListener(this);
        optionA.setOnClickListener(this);
        optionB.setOnClickListener(this);
        optionC.setOnClickListener(this);
        
        // Configurar estado inicial de los botones de opciones
        setupInitialState();
    }
    
    private void setupInitialState() {
        optionA.setVisibility(INVISIBLE);
        optionB.setVisibility(INVISIBLE);
        optionC.setVisibility(INVISIBLE);
        
        optionA.setAlpha(0f);
        optionB.setAlpha(0f);
        optionC.setAlpha(0f);
        
        optionA.setScaleX(0f);
        optionA.setScaleY(0f);
        optionB.setScaleX(0f);
        optionB.setScaleY(0f);
        optionC.setScaleX(0f);
        optionC.setScaleY(0f);
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mainMenuButton) {
            toggleMenu();
        } else {
            // Manejar clicks en opciones
            if (listener != null) {
                listener.onMenuItemClick(v.getId());
            }
            closeMenu();
        }
    }
    
    private void toggleMenu() {
        if (isMenuOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }
    
    private void openMenu() {
        isMenuOpen = true;
        
        // Hacer visibles los botones
        optionA.setVisibility(VISIBLE);
        optionB.setVisibility(VISIBLE);
        optionC.setVisibility(VISIBLE);
        
        // Animar el botón principal (cambiar a X o -)
        ObjectAnimator mainScale = ObjectAnimator.ofFloat(mainMenuButton, "scaleX", 1f, 0.95f);
        mainScale.setDuration(200);
        mainScale.start();
        
        // Animar opción A (superior)
        animateOptionOpen(optionA, DISTANCE_X_A, DISTANCE_Y_A, 100);
        
        // Animar opción B (medio)
        animateOptionOpen(optionB, DISTANCE_X_B, DISTANCE_Y_B, 200);
        
        // Animar opción C (inferior)
        animateOptionOpen(optionC, DISTANCE_X_C, DISTANCE_Y_C, 300);
    }
    
    private void animateOptionOpen(ImageButton button, int translationX, int translationY, int delay) {
        // Animación de traslación
        ObjectAnimator translateX = ObjectAnimator.ofFloat(button, "translationX", 0f, translationX);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(button, "translationY", 0f, translationY);
        
        // Animación de escala
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 0f, 1f);
        
        // Animación de alpha
        ObjectAnimator alpha = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f);
        
        // Crear AnimatorSet
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateX, translateY, scaleX, scaleY, alpha);
        animatorSet.setDuration(300);
        animatorSet.setStartDelay(delay);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        
        animatorSet.start();
    }
    
    private void closeMenu() {
        isMenuOpen = false;
        
        // Animar el botón principal de vuelta
        ObjectAnimator mainScale = ObjectAnimator.ofFloat(mainMenuButton, "scaleX", 0.95f, 1f);
        mainScale.setDuration(200);
        mainScale.start();
        
        // Animar opciones cerrándose
        animateOptionClose(optionA, 0);
        animateOptionClose(optionB, 50);
        animateOptionClose(optionC, 100);
    }
    
    private void animateOptionClose(ImageButton button, int delay) {
        // Animación de traslación de vuelta al centro
        ObjectAnimator translateX = ObjectAnimator.ofFloat(button, "translationX", button.getTranslationX(), 0f);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(button, "translationY", button.getTranslationY(), 0f);
        
        // Animación de escala
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 0f);
        
        // Animación de alpha
        ObjectAnimator alpha = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f);
        
        // Crear AnimatorSet
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateX, translateY, scaleX, scaleY, alpha);
        animatorSet.setDuration(250);
        animatorSet.setStartDelay(delay);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        
        // Ocultar al finalizar
        animatorSet.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                button.setVisibility(INVISIBLE);
            }
        });
        
        animatorSet.start();
    }
    
    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }
    
    public boolean isMenuOpen() {
        return isMenuOpen;
    }
    
    public void closeMenuIfOpen() {
        if (isMenuOpen) {
            closeMenu();
        }
    }
} 