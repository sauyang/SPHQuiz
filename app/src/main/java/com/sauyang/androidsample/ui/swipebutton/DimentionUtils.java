package com.sauyang.androidsample.ui.swipebutton;

import android.content.Context;

final class DimentionUtils {

    private DimentionUtils() {
    }

    static float converPixelsToSp(float px, Context context) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }
}
