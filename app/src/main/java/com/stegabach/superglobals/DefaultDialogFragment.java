package com.stegabach.superglobals;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by basti on 24.05.2016.
 */
public class DefaultDialogFragment extends DialogFragment {
    private EditText nameEdit;
    private String name;
    private EditText valueEdit;
    private int value;

    public interface DefaultDialogListener{
        public void onDone(DialogFragment fragment);
        public void onCancel(DialogFragment fragment);
    }

    DefaultDialogListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_default,null);

        final Bundle args = getArguments();

        nameEdit = (EditText) view.findViewById((R.id.name_edit));
        valueEdit = (EditText) view.findViewById((R.id.number_edit));

        if (args.getString(SuperglobalsContract.EXTRA_TYPE).equals(Superglobal.TYPE_SWITCH)){
            valueEdit.setVisibility(View.GONE);
        }

        //react on dialog buttons
        Button button_ok = (Button) view.findViewById(R.id.button_ok);
        Button button_cancel = (Button) view.findViewById(R.id.button_cancel);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEdit.getText().toString();
                String valueString = valueEdit.getText().toString();
                if (name.equals("")){
                    Toast.makeText(getActivity(),R.string.toast_name_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (valueString.equals("")){
                    Toast.makeText(getActivity(),R.string.toast_value_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                Superglobal superglobal;
                GlobalDatabaseHelper db = new GlobalDatabaseHelper(getActivity());
                long result = 0;
                if (args.containsKey(SuperglobalsContract.EXTRA_ID)) {
                    superglobal = db.getSuperglobalById(args.getLong(SuperglobalsContract.EXTRA_ID));
                    superglobal.setValueString(valueString);
                    result = db.updateSuperglobal(superglobal);
                }else {
                    superglobal = new Superglobal(name, Superglobal.TYPE_NUMBER,valueString);
                    result = db.createSuperglobal(superglobal);
                }
                if (result == -1) {
                    Toast.makeText(getActivity(), R.string.toast_name_duplicate, Toast.LENGTH_SHORT).show();
                    return;
                }
                IntentPusher.fireIntents(getActivity(),name,valueString);
                mListener.onDone(DefaultDialogFragment.this);
                getDialog().dismiss();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel(DefaultDialogFragment.this);
                getDialog().dismiss();
            }
        });

        //Init defaults or already defined values
        nameEdit.setText(args.getString(SuperglobalsContract.EXTRA_NAME,""));
        valueEdit.setText(args.getString(SuperglobalsContract.EXTRA_VALUE,""));

        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DefaultDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DefaultDialogListener");
        }

    }


}
