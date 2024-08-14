package ru.morozovit.util.android;

import android.content.Context;
import android.util.DisplayMetrics;

public class AndroidUtils {
    private final Context context;

    public AndroidUtils(Context context) {
        this.context = context;
    }

    public float dpToPixels(float dp) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public float dpToPixels(int dp) {
        return this.dpToPixels((float) dp);
    }

    public float dpToPixels(double dp) {
        return this.dpToPixels((float) dp);
    }

    public float pixelsToDp(int pixels) {
        return pixels / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
