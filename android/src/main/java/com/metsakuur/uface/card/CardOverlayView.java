package com.metsakuur.uface.card;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;


public class CardOverlayView extends View {
    Context context;

    private final Paint transPaint = new Paint();
    private final Paint whitePaint = new Paint();
    private final Paint redPaint = new Paint();

    @Nullable
    private float[] cornerArray;
    @Nullable
    private Integer viewWidth;
    @Nullable
    private Integer viewHeight;

    public CardOverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CardOverlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public CardOverlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (canvas != null) {
            Log.d("onDraw", "onDraw : " + this.getWidth() + " , " + this.getHeight());
            float rectLeft = 0.0F;
            float rectTop = 0.0F;
            float rectRight = 0.0F;
            float rectBottom = 0.0F;

            rectLeft = 24 * getResources().getDisplayMetrics().density;
            rectTop = 160 * getResources().getDisplayMetrics().density;
            rectRight = getWidth() - 24 * getResources().getDisplayMetrics().density;
            rectBottom =
                    160 * getResources().getDisplayMetrics().density + ((getWidth() - (24 * getResources().getDisplayMetrics().density * 2)) * 200f / 312f);

            canvas.drawRoundRect(
                    rectLeft,
                    rectTop,
                    rectRight,
                    rectBottom,
                    16 * getResources().getDisplayMetrics().density,
                    16 * getResources().getDisplayMetrics().density,
                    transPaint
            );
            canvas.drawRoundRect(
                    rectLeft,
                    rectTop,
                    rectRight,
                    rectBottom,
                    16 * getResources().getDisplayMetrics().density,
                    16 * getResources().getDisplayMetrics().density,
                    whitePaint
            );

        }
    }

    public final void setCorner(@NotNull float[] corner, int width, int height) {
        this.cornerArray = corner;
        this.viewWidth = width;
        this.viewHeight = height;
        this.invalidate();
    }

    public void init(Context context) {
        this.context = context;
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        transPaint.setAlpha(0);
        transPaint.setAntiAlias(true);
        transPaint.setColor(Color.TRANSPARENT);
        transPaint.setStyle(Paint.Style.FILL);
        transPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setStrokeWidth(getResources().getDisplayMetrics().density);

        redPaint.setColor(Color.RED);
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setStrokeWidth(getResources().getDisplayMetrics().density);
    }
}
