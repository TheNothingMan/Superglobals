package com.stegabach.superglobals;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.danielnilsson9.colorpickerview.view.ColorPanelView;

import java.util.List;

/**
 * Created by basti on 23.05.2016.
 */
public class GlobalListAdapter extends ArrayAdapter<Superglobal> {

    private List<Superglobal> superglobals;
    private Context context;

    public interface ListEditListener{
        void editClicked(Bundle args);
    }

    ListEditListener mListener;

    private SparseBooleanArray mSelection = new SparseBooleanArray();

    static class ViewHolder{
        TextView name;
        TextView value;
        ColorPanelView color;
    }

    public GlobalListAdapter(Context context, List objects) {
        super(context, R.layout.list_item, objects);

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            this.mListener = (ListEditListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ColorDialogListener");
        }
        this.context = context;
        this.superglobals = objects;
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

	/*public Set<Integer> getCurrentCheckedPosition() {
		Set<Integer> set = new Set<Integer>() {
		};
		return mSelection.keySet();
	}*/

    public void removeSelection(int position) {
        mSelection.delete(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View rowView = convertView;
        final Superglobal localGlobal = superglobals.get(position);

        if (rowView == null){
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder viewHolder = new ViewHolder();

            rowView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.name = (TextView) rowView.findViewById(R.id.list_name);
            viewHolder.value = (TextView) rowView.findViewById((R.id.list_value));
            viewHolder.color = (ColorPanelView) rowView.findViewById((R.id.list_color));

            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.name.setText(localGlobal.getName());

        //Show color for color type
        if (localGlobal.getType().equals(Superglobal.TYPE_COLOR)){
            holder.value.setVisibility(View.GONE);
            holder.color.setVisibility(View.VISIBLE);
            holder.color.setColor(localGlobal.getColorValue());
        }

        if (localGlobal.getType().equals(Superglobal.TYPE_NUMBER)){
            holder.value.setVisibility(View.VISIBLE);
            holder.color.setVisibility(View.GONE);
            holder.value.setText(localGlobal.getValueAsString());
        }

        //mark selections
        if (mSelection.get(position) == true){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }else{
            rowView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }

        //implement listener for editing
        /*View.OnClickListener listClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (MainActivity.actionModeOn) {
                    ListView listView = (ListView) parent;
                    listView.setItemChecked(position, !isPositionChecked(position));
                } else {
                    mListener.editClicked(localGlobal.getBundle());
                }

            }
        };

        rowView.setOnClickListener(listClickListener);*/

        return rowView;
    }
}
