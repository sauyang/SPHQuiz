package com.sauyang.androidsample.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.sauyang.androidsample.R;
import com.sauyang.androidsample.ui.AppApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import webservices.debug.LogDbug;

public class Utils {

    private static String RANDOM_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static int convertDPToPixels(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    static Hashtable<String,Typeface> fontCache;

    @Nullable
    public static Typeface getCachedFont(@NonNull Context context, String fontPath)
    {
        if(fontCache ==null)
            fontCache = new Hashtable<>();

        Typeface font = fontCache.get(fontPath);

        if(font!=null)
            return font;

        try
        {
            font = Typeface.createFromAsset(context.getAssets(),fontPath);
        }
        catch (Exception e)
        {
            LogDbug.printStackTrace(e);
            return null;
        }

        fontCache.put(fontPath,font);

        return font;
    }

    //Added for Component UI
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    //Added for Component UI need temporary ID to set, for example RelativeLayout set toLeftOf or toRightOf.
    public static void generateViewId(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            view.setId(generateViewId());
        else
            view.setId(View.generateViewId());
    }

    //Added for Component UI
    private static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF)
                newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static String getRandomString() {
        int numberOfSize = 20;
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(numberOfSize);
        for (int i = 0; i < numberOfSize; ++i)
            sb.append(RANDOM_CHARACTERS.charAt(random.nextInt(RANDOM_CHARACTERS.length())));
        return sb.toString();
    }

    public static String formatTimeToString(long leftMillisInFuture) {
        int seconds = (int) (leftMillisInFuture / 1000);
        int hours = seconds / (60 * 60);
        int tempMint = (seconds - (hours * 60 * 60));
        int minutes = tempMint / 60;
        seconds = tempMint - (minutes * 60);
        return "(" + seconds + "s)";
    }

    public static void setKeyboardVisible(final EditText editText, final boolean visible, final int inputMethod) {
        if (editText != null) {

            editText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (visible) {
                        editText.requestFocus();
                        //show keyboard
                        final InputMethodManager inputMethodManager = (InputMethodManager) editText
                                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(editText, inputMethod);
                    } else {
                        editText.clearFocus();
                        //hide keyboard
                        final InputMethodManager inputMethodManager = (InputMethodManager) editText
                                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                }
            }, 50);
        }
    }

    public static String formatDateForListSection(@Nullable Date date) {
        if (date == null)
            return null;
		/*String dateFormat = "dd-MMM-yyyy";
		DateFormat df = new DateFormat();
		return (String) df.format(dateFormat, date);*/
        return formatDateDBSDateFormat(date);
    }

    public static String formatDateDBSDateFormat(@Nullable Date date) {
        if (date == null)
            return "";

        Resources res = AppApplication.getAppContext().getResources();
        return DateFormat.format(res.getString(R.string.date_display_format), date).toString();
    }

    public static String formatDateCommonDateFormatter(@Nullable Date date) {
        if (date == null)
            return "";

        Resources res = AppApplication.getAppContext().getResources();
        return DateFormat.format("dd/MM/yyyy", date).toString();
    }

    public static String formatDateCommonDateFormatterMonthFirst(@Nullable Date date) {
        if (date == null)
            return "";

        Resources res = AppApplication.getAppContext().getResources();
        return DateFormat.format("MM/dd/yyyy", date).toString();
    }

    public static String formatDateHumanFriendlyDateFormatterWithoutWeek(@Nullable Date date) {
        if (date == null)
            return "";

        Resources res = AppApplication.getAppContext().getResources();
        return DateFormat.format("dd MMM yyyy", date).toString();
    }

    public static String formatDateTimeAsDDMMMYYYYHHMM(Date date) {
        if (date == null)
            return "";

        Resources resources = AppApplication.getAppContext().getResources();
        return DateFormat.format("dd MMM yyyy, HH:mm", date).toString();
    }

    public static String formatDateHumanFriendlyDateFormatter(@Nullable Date date) {
        if (date == null)
            return "";

        Resources res = AppApplication.getAppContext().getResources();
        return DateFormat.format(res.getString(R.string.date_and_week_display_format), date)
                .toString();
    }

    public static String formatDateTimeFormatter(@Nullable Date date) {
        if (date == null)
            return "";

        Resources res = AppApplication.getAppContext().getResources();
        return DateFormat.format("hh:mm a", date).toString();
    }

    public static String formatDateTimeFormatterNoAMPM(@Nullable Date date) {
        if (date == null)
            return "";

        Resources res = AppApplication.getAppContext().getResources();
        return DateFormat.format("HH:mm", date).toString();
    }

    public static Date formatDateTimeStringToDateObject(String stringDate) {
        if (!TextUtils.isEmpty(stringDate)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm.S", Locale.ENGLISH);
            Date date;
            try {
                date = dateFormat.parse(stringDate);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String formatDateServerDateFormat(@Nullable Date date) {
        if (date == null)
            return "";

        Resources res = AppApplication.getAppContext().getResources();
        return DateFormat.format("yyyy-MM-dd", date).toString();
    }

    public static String getStringFromId(@StringRes int resId) {
        return AppApplication.getInstance().getApplicationContext().getString(resId);
    }


    public static String loadJSONFromAsset(Context context, String assetFileName) {
        InputStream is = null;
        try {
            is = context.getAssets().open(assetFileName);
            BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder total = new StringBuilder(is.available());
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            return total.toString();
        } catch (IOException e) {
            Log.getStackTraceString(e);
            return null;
        } finally {
            if (is != null) {
                safeCloseStream(is);
            }
        }
    }

    public static String getStringResouceForKeyName(Context context, String resource_key)
    {
        try
        {
            int id = context.getResources().getIdentifier(resource_key, "string",
                    context.getPackageName());
            return context.getResources().getString(id);
        }
        catch (Exception e)
        {
        }
        return resource_key;
    }

    public static void safeCloseStream(InputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (Exception e) {
                Log.getStackTraceString(e);
            }
        }
    }

    public static void safeCloseStream(OutputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (Exception e) {
                Log.getStackTraceString(e);
            }
        }
    }
}
