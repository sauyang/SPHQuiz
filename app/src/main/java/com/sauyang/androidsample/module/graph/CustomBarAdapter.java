package com.sauyang.androidsample.module.graph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.sauyang.androidsample.R;

import java.util.ArrayList;
import java.util.List;

public class CustomBarAdapter extends BarChartRenderer
{

    private Context context;
    private ArrayList<Bitmap> imageList;

    public CustomBarAdapter(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, ArrayList<Bitmap> imageList, Context context) {
        super(chart, animator, viewPortHandler);
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public void drawValues(Canvas c) {
        List<IBarDataSet> dataSets = mChart.getBarData().getDataSets();
        final float valueOffsetPlus = Utils.convertDpToPixel(22f);
        float negOffset = 0f;
        float posOffset = 0f;
        boolean drawValueAboveBar = mChart.isDrawValueAboveBarEnabled();

        for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {

            IBarDataSet dataSet = dataSets.get(i);
            applyValueTextStyle(dataSet);


            boolean isInverted = mChart.isInverted(dataSet.getAxisDependency());
            float valueTextHeight = Utils.calcTextHeight(mValuePaint, "8");
//            negOffset = valueTextHeight + valueOffsetPlus;

            posOffset = (drawValueAboveBar ? -valueOffsetPlus : valueTextHeight + valueOffsetPlus);
            negOffset = (drawValueAboveBar ? valueTextHeight + valueOffsetPlus : -valueOffsetPlus);

            if (isInverted) {
                posOffset = -posOffset - valueTextHeight;
                negOffset = -negOffset - valueTextHeight;
            }

            BarBuffer buffer = mBarBuffers[i];

            float left, right, top, bottom;

            for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {

                left = buffer.buffer[j];
                right = buffer.buffer[j + 2];
                top = buffer.buffer[j + 1];
                bottom = buffer.buffer[j + 3];

                float x = (left + right) / 2f;

                if (!mViewPortHandler.isInBoundsRight(x))
                    break;

                if (!mViewPortHandler.isInBoundsY(top) || !mViewPortHandler.isInBoundsLeft(x))
                    continue;

                BarEntry entry = dataSet.getEntryForIndex(j / 4);
                float val = entry.getY();
                mValuePaint.setTextAlign(Paint.Align.CENTER);
                if (val > 0) {

//                    drawValue(c, dataSet.getValueFormatter(), val, entry, i, x,
//                            (bottom + negOffset),
//                            dataSet.getValueTextColor(j / 4));

                    drawValue(c, dataSet.getValueFormatter().getBarLabel(entry), x, val >= 0 ?
                                    (buffer.buffer[j + 1] + posOffset) :
                                    (buffer.buffer[j + 3] + negOffset),
                            dataSet.getValueTextColor(j / 4));
                }

                Bitmap bitmap = imageList.get(j / 4);

                //This is for X-axis show an array of icon instead of label
//                if (bitmap != null) {
//                    Bitmap scaledBitmap = getScaledBitmap(bitmap);
//                    c.drawBitmap(scaledBitmap, x - scaledBitmap.getWidth() / 2f, (bottom + 0.5f * negOffset) - scaledBitmap.getWidth() / 2f, null);
//                }
            }
        }
    }

    private Bitmap getScaledBitmap(Bitmap bitmap) {
        int width = (int) context.getResources().getDimensionPixelSize (R.dimen._10sdp);
        int height = (int) context.getResources().getDimensionPixelSize(R.dimen._10sdp);
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }


}

