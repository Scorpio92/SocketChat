package ru.scorpio92.socketchat.androidclient.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ru.scorpio92.socketchat.R;

public class ViewUtils {

    /**
     * Скрываем клавиатуру
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            View focusedView = activity.getCurrentFocus();
            if(focusedView != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                IBinder windowToken = focusedView.getWindowToken();
                if (inputMethodManager != null)
                    inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

    /**
     * Показываем тост
     */
    public static void showToast(Context context, String text) {
        if (context != null && text != null && !text.isEmpty())
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Показываем SnackBar
     */
    public static void showShackBar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        TextView tv = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        snackbar.show();
    }

    public static AlertDialog.Builder getDialogBuilder(@NonNull Context context, @Nullable String title, @Nullable String msg) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg);
    }

    public static void createAndShowAlertDialog(@NonNull Context context, @Nullable String title, @Nullable String msg) {
        createAndShowAlertDialog(context, title, msg, false, (dialogInterface, i) -> dialogInterface.dismiss());
    }

    public static void createAndShowAlertDialog(@NonNull Context context, @Nullable String title, @Nullable String msg, boolean isModalDialog, @Nullable DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = getDialogBuilder(context, title, msg);
        if (listener != null)
            builder.setPositiveButton(context.getString(android.R.string.ok), listener);
        builder.setCancelable(!isModalDialog);
        builder.show();
    }

    public static AlertDialog showProgressDialog(@NonNull Context context, @Nullable String title) {
        AlertDialog.Builder builder = getDialogBuilder(context, title, context.getString(R.string.progress_dialog_msg));
        builder.setCancelable(false);
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(layoutParams);
        progressBar.setPadding(0, 0, 0, 24);
        builder.setView(progressBar);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (dialog.getWindow() != null) {
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = 0.85f;
            dialog.getWindow().setAttributes(lp);
        }
        return dialog;
    }

    public static void safetyDismissDialog(AlertDialog dialog) {
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public static void replaceFragment(FragmentManager fragmentManager, int containerId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    public static void clearFragmentStack(FragmentManager fragmentManager) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
