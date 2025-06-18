package com.example.kirfvet.components;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.kirfvet.R;

public class EditButton extends RelativeLayout {
    private ImageView editIcon;
    private GradientDrawable backgroundDrawable;
    private View blurOverlay;
    private boolean isPressed = false;
    private Paint underlinePaint;
    private float underlineProgress = 0f;

    public EditButton(Context context) {
        super(context);
        init(context);
    }

    public EditButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setupButton();
        setupBlurOverlay(context);
        setupEditIcon(context);
        setupUnderlineView(context);
        setupTouchListener();
        setWillNotDraw(false);
    }

    private void setupButton() {
        backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
        backgroundDrawable.setColor(Color.rgb(93, 93, 116));
        float cornerRadius = 20 * getContext().getResources().getDisplayMetrics().density;
        backgroundDrawable.setCornerRadius(cornerRadius);
        setBackground(backgroundDrawable);
        setElevation(8f);
        int size = (int) (55 * getContext().getResources().getDisplayMetrics().density);
        setLayoutParams(new RelativeLayout.LayoutParams(size, size));
        setClipToOutline(true);
    }

    private void setupBlurOverlay(Context context) {
        blurOverlay = new View(context) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                if (getScaleX() > 0) {
                    Paint paint = new Paint();
                    paint.setColor(Color.rgb(102, 102, 141));
                    paint.setAntiAlias(true);
                    float centerX = getWidth() / 2f;
                    float centerY = getHeight() / 2f;
                    float radius = Math.min(getWidth(), getHeight()) / 2f;
                    canvas.drawCircle(centerX, centerY, radius, paint);
                }
            }
        };
        RelativeLayout.LayoutParams overlayParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        );
        blurOverlay.setLayoutParams(overlayParams);
        blurOverlay.setScaleX(0f);
        blurOverlay.setScaleY(0f);
        addView(blurOverlay);
    }

    private void setupEditIcon(Context context) {
        editIcon = new ImageView(context);
        editIcon.setImageResource(R.drawable.ic_edit);
        editIcon.setColorFilter(Color.WHITE);
        RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
            (int) (17 * getContext().getResources().getDisplayMetrics().density),
            (int) (17 * getContext().getResources().getDisplayMetrics().density)
        );
        iconParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        editIcon.setLayoutParams(iconParams);
        addView(editIcon);
    }

    private void setupUnderlineView(Context context) {
        underlinePaint = new Paint();
        underlinePaint.setColor(Color.WHITE);
        underlinePaint.setStrokeWidth(1.5f * getContext().getResources().getDisplayMetrics().density);
        underlinePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (underlineProgress > 0) {
            float density = getContext().getResources().getDisplayMetrics().density;
            float lineWidth = 25 * density;
            float lineHeight = 1.5f * density;
            float bottom = getHeight() - (19 * density);
            float currentWidth = lineWidth * underlineProgress;
            float startX = (getWidth() - currentWidth) / 2f;
            float endX = startX + currentWidth;
            canvas.drawLine(startX, bottom, endX, bottom, underlinePaint);
        }
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
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                performClick();
                            }
                        }, 100);
                        break;
                }
                return true;
            }
        });
    }

    private void startHoverAnimation() {
        ObjectAnimator overlayScaleX = ObjectAnimator.ofFloat(blurOverlay, "scaleX", 0f, 1f);
        ObjectAnimator overlayScaleY = ObjectAnimator.ofFloat(blurOverlay, "scaleY", 0f, 1f);
        overlayScaleX.setDuration(300);
        overlayScaleY.setDuration(300);
        ObjectAnimator iconRotation = ObjectAnimator.ofFloat(editIcon, "rotation", 0f, -15f);
        ObjectAnimator iconTranslationX = ObjectAnimator.ofFloat(editIcon, "translationX", 0f, 5 * getContext().getResources().getDisplayMetrics().density);
        iconRotation.setDuration(200);
        iconTranslationX.setDuration(200);
        ValueAnimator underlineAnimator = ValueAnimator.ofFloat(0f, 1f);
        underlineAnimator.setDuration(500);
        underlineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                underlineProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        ObjectAnimator elevationAnimator = ObjectAnimator.ofFloat(this, "elevation", 8f, 15f);
        elevationAnimator.setDuration(300);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(overlayScaleX, overlayScaleY, iconRotation, iconTranslationX, underlineAnimator, elevationAnimator);
        animatorSet.start();
    }

    private void startNormalAnimation() {
        ObjectAnimator overlayScaleX = ObjectAnimator.ofFloat(blurOverlay, "scaleX", 1f, 0f);
        ObjectAnimator overlayScaleY = ObjectAnimator.ofFloat(blurOverlay, "scaleY", 1f, 0f);
        overlayScaleX.setDuration(300);
        overlayScaleY.setDuration(300);
        ObjectAnimator iconRotation = ObjectAnimator.ofFloat(editIcon, "rotation", -15f, 0f);
        ObjectAnimator iconTranslationX = ObjectAnimator.ofFloat(editIcon, "translationX", 5 * getContext().getResources().getDisplayMetrics().density, 0f);
        iconRotation.setDuration(200);
        iconTranslationX.setDuration(200);
        ValueAnimator underlineAnimator = ValueAnimator.ofFloat(1f, 0f);
        underlineAnimator.setDuration(300);
        underlineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                underlineProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        ObjectAnimator elevationAnimator = ObjectAnimator.ofFloat(this, "elevation", 15f, 8f);
        elevationAnimator.setDuration(300);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(overlayScaleX, overlayScaleY, iconRotation, iconTranslationX, underlineAnimator, elevationAnimator);
        animatorSet.start();
    }

    public void setOnEditClickListener(OnClickListener listener) {
        setOnClickListener(listener);
    }
} 