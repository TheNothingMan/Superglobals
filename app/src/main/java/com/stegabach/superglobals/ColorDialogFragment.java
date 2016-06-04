package com.stegabach.superglobals;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.danielnilsson9.colorpickerview.view.ColorPanelView;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;

/**
 * Created by basti on 22.05.2016.
 */
public class ColorDialogFragment extends DialogFragment{

    private ColorPanelView oldPanel;
    private ColorPanelView newPanel;
    private ColorPickerView colorPicker;
    private EditText hexEdit;
    private String hexValue;
    private EditText nameEdit;
    private String name;

    public interface ColorDialogListener{
        public void onColorPicked(DialogFragment fragment);
        public void onCancel(DialogFragment fragment);
    }

    ColorDialogListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_color,null);

        final Bundle args = getArguments();

        oldPanel = (ColorPanelView) view.findViewById(R.id.colorpickerview__color_panel_old);
        newPanel = (ColorPanelView) view.findViewById(R.id.colorpickerview__color_panel_new);
        colorPicker = (ColorPickerView) view.findViewById(R.id.colorpickerview__color_picker_view);
        hexEdit = (EditText) view.findViewById(R.id.hex_edit);
        nameEdit = (EditText) view.findViewById((R.id.name_edit));
        //update the hex field
        colorPicker.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int newColor) {
                newPanel.setColor(newColor);
                updateText(newColor);
            }
        });

        //get input from hex field and update color picker
        hexEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    updateColorFromHex();
                }
                return false;
            }
        });

        hexEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    updateColorFromHex();
                }
            }
        });

        //react on dialog buttons
        Button button_ok = (Button) view.findViewById(R.id.button_ok);
        Button button_cancel = (Button) view.findViewById(R.id.button_cancel);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEdit.getText().toString();
                if (name.equals("")){
                    Toast.makeText(getActivity(),R.string.toast_name_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                Superglobal superglobal;
                GlobalDatabaseHelper db = new GlobalDatabaseHelper(getActivity());
                long result = 0;
                if (args.containsKey(SuperglobalsContract.EXTRA_ID)) {
                    superglobal = db.getSuperglobalById(args.getLong(SuperglobalsContract.EXTRA_ID));
                    superglobal.setValueString(hexValue);
                    superglobal.setName(name);
                    result = db.updateSuperglobal(superglobal);
                }else {
                    superglobal = new Superglobal(name, Superglobal.TYPE_COLOR, hexValue);
                    result = db.createSuperglobal(superglobal);
                }
                if (result == -1) {
                    Toast.makeText(getActivity(), R.string.toast_name_duplicate, Toast.LENGTH_SHORT).show();
                    return;
                }
                IntentPusher.fireIntents(getActivity(),name,hexValue);
                mListener.onColorPicked(ColorDialogFragment.this);
                getDialog().dismiss();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel(ColorDialogFragment.this);
                getDialog().dismiss();
            }
        });

        //Init defaults or already defined values
        colorPicker.setColor(args.getInt(SuperglobalsContract.EXTRA_COLOR_INT,SuperglobalsContract.DEFAULT_COLOR));
        oldPanel.setColor(args.getInt(SuperglobalsContract.EXTRA_COLOR_INT,SuperglobalsContract.DEFAULT_COLOR));
        newPanel.setColor(args.getInt(SuperglobalsContract.EXTRA_COLOR_INT,SuperglobalsContract.DEFAULT_COLOR));
        nameEdit.setText(args.getString(SuperglobalsContract.EXTRA_NAME,""));
        updateText(args.getInt(SuperglobalsContract.EXTRA_COLOR_INT,SuperglobalsContract.DEFAULT_COLOR));

        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ColorDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ColorDialogListener");
        }

    }

    private void updateText(int color){
        hexValue = "#"+Integer.toHexString(color);
        hexEdit.setText(hexValue);
    }

    private void updateColorFromHex(){
        try {
            hexValue = hexEdit.getText().toString();
            colorPicker.setColor(Color.parseColor(hexValue));
            newPanel.setColor(colorPicker.getColor());
        }catch (Exception e) {
            Log.d("colorPicker", "onEditorAction: " + e);
        }
    }
}
