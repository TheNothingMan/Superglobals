package com.stegabach.superglobals;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_color,null);

        oldPanel = (ColorPanelView) view.findViewById(R.id.colorpickerview__color_panel_old);
        newPanel = (ColorPanelView) view.findViewById(R.id.colorpickerview__color_panel_new);
        colorPicker = (ColorPickerView) view.findViewById(R.id.colorpickerview__color_picker_view);
        hexEdit = (EditText) view.findViewById(R.id.hex_edit);


        colorPicker.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int newColor) {
                newPanel.setColor(newColor);
                updateText(newColor);
            }
        });

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

        builder.setView(view)
        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ColorDialogFragment.this.getDialog().cancel();
            }
        })
        ;

        return builder.create();
    }

    private void updateText(int color){
        hexValue = "#"+Integer.toHexString(color);
        hexEdit.setText(hexValue);
    }

    private void updateColorFromHex(){
        try {
            colorPicker.setColor(Color.parseColor(hexEdit.getText().toString()));
            newPanel.setColor(colorPicker.getColor());
        }catch (Exception e) {
            Log.d("colorPicker", "onEditorAction: " + e);
        }
    }
}
