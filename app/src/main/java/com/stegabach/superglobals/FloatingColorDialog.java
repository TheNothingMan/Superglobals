package com.stegabach.superglobals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;


/**
 * Created by basti on 30.05.2016.
 */
public class FloatingColorDialog extends StandOutWindow {

    public static final String FLOATING_DIALOG_COLOR_CHANGED = "floatingDialogColorChanged";
    public static final String FLOATING_DIALOG_COLOR_RETURN = "floatingDialogColorReturn";
    public static final String FLOATING_DIALOG_COLOR_VAR = "floatingDialogColorVar";
    private static final String PREFERENCES = "MyPreferences";
    private boolean edited = false;
    private SharedPreferences mPrefs;

    static /* synthetic */ void access$2(FloatingColorDialog floatingColorDialog, boolean bl) {
        floatingColorDialog.edited = bl;
    }

    /*private void resetColor() {
        Context context = this.getApplicationContext();
        String string2 = this.mPrefs.getString("FLOATING_VAR", "");
        Object[] arrobject = new Object[]{this.mPrefs.getInt("FLOATING_COLOR", 0)};
        ZwIntentPusher.SendDataToZW(context, string2, String.format((String)"%08x", (Object[])arrobject));
    }

    private void resetColor(ColorPicker colorPicker) {
        colorPicker.setColor(this.mPrefs.getInt("FLOATING_COLOR", 0));
        super.resetColor();
    }*/

    @Override
    public void createAndAttachView(int n, FrameLayout frameLayout) {
        View view = ((LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.colorpickerview__dialog_color_picker,null);
        final ColorPickerView colorPicker = (ColorPickerView) view.findViewById(R.id.colorpickerview__color_picker_view);
    }

    @Override
    public int getAppIcon() {
        return 2130837520;
    }

    @Override
    public String getAppName() {
        /*if (this.mPrefs == null) {
            this.mPrefs = PreferenceManager.getDefaultSharedPreferences((Context)this.getApplicationContext());
        }*/
        //return "#T" + this.mPrefs.getString("FLOATING_VAR", "") + "#";
        return "Superglobals";
    }

    @Override
    public int getFlags(int n) {
        return super.getFlags(n) | StandOutFlags.FLAG_BODY_MOVE_ENABLE | StandOutFlags.FLAG_WINDOW_FOCUSABLE_DISABLE | StandOutFlags.FLAG_DECORATION_MAXIMIZE_DISABLE | StandOutFlags.FLAG_DECORATION_RESIZE_DISABLE | StandOutFlags.FLAG_ADD_FUNCTIONALITY_DROP_DOWN_DISABLE | StandOutFlags.FLAG_DECORATION_SYSTEM;
    }

    @Override
    public StandOutWindow.StandOutLayoutParams getParams(int n, Window window) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)this.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        float f = displayMetrics.density;
        return new StandOutWindow.StandOutLayoutParams(n, (int)(0.5f + 350.0f * f), (int)(0.5f + 230.0f * f), Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    @Override
    public Intent getPersistentNotificationIntent(int n) {
        return StandOutWindow.getCloseIntent((Context)this, FloatingColorDialog.class, n);
    }

    @Override
    public String getPersistentNotificationMessage(int n) {
        return "Click to close";
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean onClose(int n, Window window) {
        if (!this.edited) {
        } else {
        }
        /*if (!this.mPrefs.getBoolean("FLOATING_INTENT", true)) {
            Intent intent = new Intent((Context)this, (Class)MainActivity.class);
            intent.setFlags(268468224);
            this.startActivity(intent);
        }*/
        return super.onClose(n, window);
    }



}
