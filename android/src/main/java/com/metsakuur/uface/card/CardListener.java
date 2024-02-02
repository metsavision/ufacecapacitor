package com.metsakuur.uface.card;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;

public interface CardListener {
    void onCardBitmap(@NotNull Bitmap bitmap);

    void onCardCorner(@NotNull float[] corner, int width, int height);
}
