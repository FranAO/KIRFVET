package com.example.kirfvet.animations;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kirfvet.R;

public class AnimatedLogoutButton extends FrameLayout {
    private ImageView iconView;
    private TextView textView;
    private boolean isExpanded = false;
    private ValueAnimator animator;
    private static final int INITIAL_SIZE = 45;
    private static final int EXPANDED_SIZE = 125;

    public AnimatedLogoutButton(@NonNull Context context) {
        super(context);
        init();
    }

    public AnimatedLogoutButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_animated_logout_button, this);
        iconView = findViewById(R.id.logout_icon);
        textView = findViewById(R.id.logout_text);
        
        setBackgroundResource(R.drawable.btn_logout_background);
        setElevation(8f);
        
        // Set initial size
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(INITIAL_SIZE, INITIAL_SIZE);
        } else {
            params.width = INITIAL_SIZE;
            params.height = INITIAL_SIZE;
        }
        setLayoutParams(params);
        
        // Make sure the view is clickable
        setClickable(true);
        setFocusable(true);
        
        // Initialize text view
        textView.setVisibility(View.GONE);
        textView.setAlpha(0f);
    }

    private void expandButton() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        animator = ValueAnimator.ofInt(INITIAL_SIZE, EXPANDED_SIZE);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = value;
            setLayoutParams(params);
            requestLayout();
            
            // Animate text
            float progress = (float) (value - INITIAL_SIZE) / (EXPANDED_SIZE - INITIAL_SIZE);
            textView.setAlpha(progress);
            if (progress > 0) {
                textView.setVisibility(View.VISIBLE);
            }
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                isExpanded = true;
                textView.setVisibility(View.VISIBLE);
                textView.setAlpha(1f);
            }
        });

        animator.start();
    }

    private void collapseButton() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        animator = ValueAnimator.ofInt(EXPANDED_SIZE, INITIAL_SIZE);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = value;
            setLayoutParams(params);
            requestLayout();
            
            // Animate text
            float progress = (float) (value - INITIAL_SIZE) / (EXPANDED_SIZE - INITIAL_SIZE);
            textView.setAlpha(progress);
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                isExpanded = false;
                textView.setVisibility(View.GONE);
                textView.setAlpha(0f);
            }
        });

        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setTranslationX(2f);
                setTranslationY(2f);
                if (!isExpanded) {
                    expandButton();
                }
                return true;
                
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setTranslationX(0f);
                setTranslationY(0f);
                if (isExpanded) {
                    collapseButton();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }
} 