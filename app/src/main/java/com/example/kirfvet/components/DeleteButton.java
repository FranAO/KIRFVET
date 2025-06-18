package com.example.kirfvet.components;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.kirfvet.R;

public class DeleteButton extends RelativeLayout {
    private ImageView binTop;
    private ImageView binBottom;
    private GradientDrawable backgroundDrawable;
    private boolean isPressed = false;
    
    public DeleteButton(Context context) {
        super(context);
        init(context);
    }
    
    public DeleteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    public DeleteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    private void init(Context context) {
        setupButton();
        setupImageViews(context);
        setupTouchListener();
    }
    
    private void setupButton() {
        backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setShape(GradientDrawable.OVAL);
        backgroundDrawable.setColor(Color.rgb(20, 20, 20));
        
        setBackground(backgroundDrawable);
        setElevation(10f);
        
        int size = (int) (50 * getContext().getResources().getDisplayMetrics().density);
        setLayoutParams(new RelativeLayout.LayoutParams(size, size));
    }
    
    private void setupImageViews(Context context) {
        binTop = new ImageView(context);
        binTop.setImageResource(R.drawable.ic_bin_top);
        binTop.setColorFilter(Color.WHITE);
        
        RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(
            (int) (12 * getContext().getResources().getDisplayMetrics().density),
            (int) (12 * getContext().getResources().getDisplayMetrics().density)
        );
        topParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        topParams.topMargin = -5;
        binTop.setLayoutParams(topParams);
        
        binBottom = new ImageView(context);
        binBottom.setImageResource(R.drawable.ic_bin_bottom);
        binBottom.setColorFilter(Color.WHITE);
        
        RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(
            (int) (12 * getContext().getResources().getDisplayMetrics().density),
            (int) (12 * getContext().getResources().getDisplayMetrics().density)
        );
        bottomParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        bottomParams.topMargin = 5;
        binBottom.setLayoutParams(bottomParams);
        
        addView(binBottom);
        addView(binTop);
    }
    
    private void setupTouchListener() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isPressed) {
                            isPressed = true;
                            startHoverAnimation();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (isPressed) {
                            isPressed = false;
                            startNormalAnimation();
                        }
                        break;
                }
                return true;
            }
        });
    }
    
    private void startHoverAnimation() {
        ObjectAnimator backgroundAnimator = ObjectAnimator.ofArgb(
            backgroundDrawable, "color", Color.rgb(20, 20, 20), Color.rgb(255, 69, 69)
        );
        backgroundAnimator.setDuration(300);
        
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(
            binTop, "rotation", 0f, 160f
        );
        rotationAnimator.setDuration(500);
        
        binTop.setPivotX(binTop.getWidth() * 0.8f);
        binTop.setPivotY(binTop.getHeight() * 0.8f);
        
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, rotationAnimator);
        animatorSet.start();
    }
    
    private void startNormalAnimation() {
        ObjectAnimator backgroundAnimator = ObjectAnimator.ofArgb(
            backgroundDrawable, "color", Color.rgb(255, 69, 69), Color.rgb(20, 20, 20)
        );
        backgroundAnimator.setDuration(300);
        
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(
            binTop, "rotation", 160f, 0f
        );
        rotationAnimator.setDuration(300);
        
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, rotationAnimator);
        animatorSet.start();
    }
    
    public void setOnDeleteClickListener(OnClickListener listener) {
        setOnClickListener(listener);
    }
} 