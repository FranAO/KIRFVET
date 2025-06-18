package com.example.kirfvet.navigation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.HashMap;
import java.util.Map;

public class CustomBottomNavigationView extends BottomNavigationView {
    private View activeItem;
    private AnimatorSet currentAnimator;
    private Map<Integer, View> menuItems;

    public CustomBottomNavigationView(Context context) {
        super(context);
        init();
    }

    public CustomBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        menuItems = new HashMap<>();
        setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            View newActiveItem = findViewById(itemId);
            
            if (newActiveItem != null) {
                // Guardar la referencia del ítem si no existe
                if (!menuItems.containsKey(itemId)) {
                    menuItems.put(itemId, newActiveItem);
                }

                if (newActiveItem == activeItem) {
                    // Si se selecciona el mismo ítem, animar de vuelta a la posición original
                    animateItemReturn(activeItem);
                    activeItem = null;
                } else {
                    // Si hay un ítem activo, animarlo de vuelta
                    if (activeItem != null) {
                        animateItemReturn(activeItem);
                    }
                    // Animar el nuevo ítem
                    animateItem(newActiveItem);
                    activeItem = newActiveItem;
                }

                // Asegurarse de que todos los demás ítems estén en su posición original
                for (Map.Entry<Integer, View> entry : menuItems.entrySet()) {
                    if (entry.getValue() != activeItem) {
                        resetItemPosition(entry.getValue());
                    }
                }
            }
            return true;
        });
    }

    private void animateItem(View item) {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(item, "scaleX", 1f, 1.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(item, "scaleY", 1f, 1.2f);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(item, "translationY", 0f, -20f);

        currentAnimator = new AnimatorSet();
        currentAnimator.playTogether(scaleX, scaleY, translateY);
        currentAnimator.setDuration(300);
        currentAnimator.setInterpolator(new DecelerateInterpolator());
        currentAnimator.start();
    }

    private void animateItemReturn(View item) {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(item, "scaleX", 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(item, "scaleY", 1.2f, 1f);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(item, "translationY", -20f, 0f);

        currentAnimator = new AnimatorSet();
        currentAnimator.playTogether(scaleX, scaleY, translateY);
        currentAnimator.setDuration(300);
        currentAnimator.setInterpolator(new DecelerateInterpolator());
        currentAnimator.start();
    }

    private void resetItemPosition(View item) {
        item.setScaleX(1f);
        item.setScaleY(1f);
        item.setTranslationY(0f);
    }
} 