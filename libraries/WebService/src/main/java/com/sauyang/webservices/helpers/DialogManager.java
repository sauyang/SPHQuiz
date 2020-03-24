package com.sauyang.webservices.helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;

import com.sauyang.webservices.R;

import java.util.HashSet;

import webservices.debug.LogDbug;

public class DialogManager {

    private static HashSet<CharSequence> currentlyShowingMessages = new HashSet<>();

    @Nullable
    public static AlertDialog showAlertDialogBuilder(@NonNull AlertDialog.Builder alert)
    {
        try {
            return alert.show();
        }
        catch (Exception e) {
            LogDbug.printStackTrace(e);
            return null;
        }
    }

    @Nullable
    public static AlertDialog showAlertDialog(@NonNull Context context,
                                              @Nullable CharSequence title,
                                              @NonNull CharSequence message)
    {
        return showAlertDialog(context, title, message, context.getResources().getString(R.string.ok),null,null,null);
    }

    /**
     * Safely checks if fragment is null or detached
     * @param fragment
     * @param title
     * @param message
     * @return
     */
    @Nullable
    public static AlertDialog showAlertDialog(@NonNull Fragment fragment,
                                              @Nullable CharSequence title,
                                              @NonNull CharSequence message)
    {
        if(fragment == null || !fragment.isAdded())
            return null;

        return showAlertDialog(fragment.getActivity(), title, message);
    }

    @Nullable
    public static AlertDialog showAlertDialog(@NonNull Context context,
                                              @Nullable CharSequence title,
                                              @NonNull final CharSequence message,
                                              @Nullable String buttonTitle)
    {
        return showAlertDialog(context, title, message, buttonTitle, null, null, null);
    }

    @Nullable
    public static AlertDialog showAlertDialog(@NonNull Context context,
                                              @Nullable CharSequence title,
                                              @NonNull final CharSequence message,
                                              @Nullable String buttonTitle,
                                              @Nullable final DialogInterface.OnClickListener buttonListener){
        return showAlertDialog(context,title,message,buttonTitle,buttonListener,null,null);
    }

    @Nullable
    public static AlertDialog showAlertDialog(@NonNull Context context,
                                              @Nullable CharSequence title,
                                              @NonNull final CharSequence message,
                                              @Nullable String positiveButtonTitle,
                                              @Nullable final DialogInterface.OnClickListener positiveButtonListener,
                                              @Nullable String negativeButtonTitle,
                                              @Nullable final DialogInterface.OnClickListener negativeButtonListener)
    {
        if (context != null && message!=null && !currentlyShowingMessages.contains(message)){

            AlertDialog.Builder b =new AlertDialog.Builder(context);

            if(title!=null)
                b.setTitle(title);

            b.setMessage(message);
            b.setCancelable(false);

            if(positiveButtonTitle!=null)
                b.setPositiveButton(positiveButtonTitle, positiveButtonListener);

            if(negativeButtonTitle!=null)
                b.setNegativeButton(negativeButtonTitle, negativeButtonListener);

//            b.setIcon(android.R.drawable.ic_dialog_alert);

            currentlyShowingMessages.add(message);

            AlertDialog dialog = showAlertDialogBuilder(b);

            if(dialog != null) {
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        currentlyShowingMessages.remove(message);
                    }
                });
            }

            return dialog;
        }
        return null;
    }


    private static Dialog customProgressDialog = null;

    public static void showProgressDialog(Context context)
    {
        showProgressDialog(context, context.getString(R.string.loading_data));
    }

    public static void showProgressDialog(Context context, String message)
    {
        if (customProgressDialog == null)
            customProgressDialog = CustomProgressDialog.show(context, message);
        else if (!customProgressDialog.isShowing())
            customProgressDialog.show();
    }

    public static void hideProgressDialog(){

        try {
            if(customProgressDialog != null && customProgressDialog.isShowing()){
                customProgressDialog.dismiss();
            }
        }
        catch (Exception e){}
        finally {
            customProgressDialog = null;
        }
    }

}

/**
 * A custom ProgressDialog that can be themed for appcompat and material design
 * (ProgressDialog cannot be themed)
 */
class CustomProgressDialog extends AppCompatDialog {

    public static CustomProgressDialog show(Context context) {
        return show(context, null);
    }

    public static CustomProgressDialog show(Context context,
                                            CharSequence message) {
        return show(context, message, true);
    }


    public static CustomProgressDialog show(Context context,
                                            CharSequence message, boolean indeterminate) {
        return show(context, message, indeterminate, false, null);
    }

    public static CustomProgressDialog show(Context context,
                                            CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, message, indeterminate, cancelable, null);
    }

    public static CustomProgressDialog show(Context context,
                                            CharSequence message, boolean indeterminate,
                                            boolean cancelable, DialogInterface.OnCancelListener cancelListener) {

        LayoutInflater LayoutInflater = (android.view.LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.inflate(R.layout.custom_progress_dialog, null);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        textView.setText(message);
        progressBar.setIndeterminate(indeterminate);

        CustomProgressDialog dialog = new CustomProgressDialog(context);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        /* The next line will add the ProgressBar to the dialog. */
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }

    public CustomProgressDialog(Context context) {
        super(context,R.style.ProgressDialogStyleNoTitle);
    }
}
