package com.example.kirfvet;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    private static final int SPLASH_DELAY = 5200; // Reducido a 4 segundos total
    private View hole;
    private ImageView logo;
    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Inicializar vistas
        hole = findViewById(R.id.hole);
        logo = findViewById(R.id.logo);
        titleText = findViewById(R.id.titleText);

        // Iniciar secuencia de animación
        startAnimationSequence();
    }

    private void startAnimationSequence() {
        // Hacer visible el logo y establecer su tamaño inicial pequeño
        logo.setVisibility(View.VISIBLE);
        logo.setScaleX(0.5f);
        logo.setScaleY(0.5f);

        // 1. El logo sale del círculo con una aceleración suave
        ObjectAnimator logoUp = ObjectAnimator.ofFloat(logo, "translationY", 0f, -200f);
        logoUp.setDuration(500); // Reducido a 0.5 segundos
        logoUp.setInterpolator(new AccelerateDecelerateInterpolator());

        // 2. El logo sigue subiendo con una aceleración más pronunciada
        ObjectAnimator logoUpMore = ObjectAnimator.ofFloat(logo, "translationY", -200f, -800f);
        logoUpMore.setDuration(800); // Reducido a 0.8 segundos
        logoUpMore.setInterpolator(new AccelerateInterpolator(1.2f)); // Aceleración más suave

        // El logo crece mientras sube con un efecto suave
        ObjectAnimator logoScaleUp = ObjectAnimator.ofFloat(logo, "scaleX", 0.5f, 1.3f);
        ObjectAnimator logoScaleUpY = ObjectAnimator.ofFloat(logo, "scaleY", 0.5f, 1.3f);
        logoScaleUp.setDuration(1300); // Reducido a 1.3 segundos
        logoScaleUpY.setDuration(1300);
        logoScaleUp.setInterpolator(new OvershootInterpolator(0.3f)); // Rebote más sutil
        logoScaleUpY.setInterpolator(new OvershootInterpolator(0.3f));

        // El agujero se cierra cuando el logo llega arriba con una desaceleración suave
        ObjectAnimator holeScaleX = ObjectAnimator.ofFloat(hole, "scaleX", 1f, 0f);
        ObjectAnimator holeScaleY = ObjectAnimator.ofFloat(hole, "scaleY", 1f, 0f);
        holeScaleX.setDuration(800); // Reducido a 0.8 segundos
        holeScaleY.setDuration(800);
        holeScaleX.setInterpolator(new DecelerateInterpolator(1.2f)); // Desaceleración más suave
        holeScaleY.setInterpolator(new DecelerateInterpolator(1.2f));

        // 3. El logo se mueve al centro con una desaceleración suave
        ObjectAnimator logoToCenter = ObjectAnimator.ofFloat(logo, "translationY", -800f, 0f);
        logoToCenter.setDuration(1000); // Reducido a 1 segundo
        logoToCenter.setInterpolator(new DecelerateInterpolator(1.0f));

        // Reducir el tamaño del logo durante su bajada con un efecto suave
        ObjectAnimator logoScaleDown = ObjectAnimator.ofFloat(logo, "scaleX", 1.3f, 1f);
        ObjectAnimator logoScaleDownY = ObjectAnimator.ofFloat(logo, "scaleY", 1.3f, 1f);
        logoScaleDown.setDuration(1000); // Reducido a 1 segundo
        logoScaleDownY.setDuration(1000);
        logoScaleDown.setInterpolator(new DecelerateInterpolator(1.0f));
        logoScaleDownY.setInterpolator(new DecelerateInterpolator(1.0f));

        // 4. El logo se mueve a la izquierda con un efecto suave
        ObjectAnimator logoToLeft = ObjectAnimator.ofFloat(logo, "translationX", 0f, -200f);
        logoToLeft.setDuration(500); // Reducido a 0.5 segundos
        logoToLeft.setInterpolator(new AccelerateDecelerateInterpolator());

        // 5. El texto aparece gradualmente con un efecto suave
        ObjectAnimator textFadeIn = ObjectAnimator.ofFloat(titleText, "alpha", 0f, 1f);
        textFadeIn.setDuration(600); // Reducido a 0.6 segundos
        textFadeIn.setInterpolator(new DecelerateInterpolator());

        // Crear secuencia de animación
        AnimatorSet animatorSet = new AnimatorSet();
        // Primera fase: logo sale del círculo
        animatorSet.play(logoUp);
        // Segunda fase: logo sigue subiendo y crece
        animatorSet.play(logoUpMore).with(logoScaleUp).with(logoScaleUpY).after(logoUp);
        // El agujero se cierra cuando el logo llega arriba
        animatorSet.play(holeScaleX).with(holeScaleY).with(logoUpMore);
        // Esperar 1.5 segundos antes de bajar (reducido de 1.94)
        animatorSet.play(logoToCenter).with(logoScaleDown).with(logoScaleDownY).after(logoUpMore).after(1500);
        // Esperar 0.2 segundos en el centro antes de moverse a la izquierda (reducido de 0.3)
        animatorSet.play(logoToLeft).after(logoToCenter).after(200);
        // Quinta fase: texto aparece
        animatorSet.play(textFadeIn).after(logoToLeft);

        // Iniciar animaciones
        animatorSet.start();

        // Navegar a MainActivity después del retraso
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash.this, Presentacion.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}