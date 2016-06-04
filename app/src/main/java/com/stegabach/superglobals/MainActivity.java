package com.stegabach.superglobals;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity implements ColorDialogFragment.ColorDialogListener,GlobalListAdapter.ListEditListener,DefaultDialogFragment.DefaultDialogListener {

    private FabSpeedDial fab;
    private ListView globalsList;
    private GlobalDatabaseHelper db;
    private List<Superglobal> superglobals;
    private GlobalListAdapter listAdapter;

    public List<Integer> selectedItems;
    public static boolean actionModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Init the Floating Action Button
        fab = (FabSpeedDial) findViewById(R.id.fab_main);
        fab.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {

                Bundle args = new Bundle();
                switch (menuItem.getItemId()){
                    case R.id.action_add_color:
                        ColorDialogFragment colorDialog = new ColorDialogFragment();
                        args.putString(SuperglobalsContract.EXTRA_TYPE, Superglobal.TYPE_COLOR);
                        colorDialog.setArguments(args);
                        colorDialog.show(getFragmentManager(),"dialog");
                        break;
                    case R.id.action_add_number:
                        DefaultDialogFragment numberDialog = new DefaultDialogFragment();
                        args.putString(SuperglobalsContract.EXTRA_TYPE, Superglobal.TYPE_NUMBER);
                        numberDialog.setArguments(args);
                        numberDialog.show(getFragmentManager(),"dialog");
                        break;
                    case R.id.action_add_switch:
                        DefaultDialogFragment switchDialog = new DefaultDialogFragment();
                        args.putString(SuperglobalsContract.EXTRA_TYPE, Superglobal.TYPE_SWITCH);
                        switchDialog.setArguments(args);
                        switchDialog.show(getFragmentManager(),"dialog");
                        break;
                }

                return super.onMenuItemSelected(menuItem);
            }
        });

        //Init the database and list of items
        db = new GlobalDatabaseHelper(this);
        globalsList= (ListView) findViewById(R.id.main_list);
        refreshList();

        //Init Multiselection
        selectedItems = new ArrayList<Integer>();
        globalsList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        globalsList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private int nr = 0;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

                listAdapter.clearSelection();
                selectedItems.clear();
                actionModeOn = false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context, menu);
                nr = 0;
                actionModeOn = true;
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        for (int i : selectedItems) {
                            db.deleteSuperglobal(superglobals.get(i).getId());
                        }
                        listAdapter.clearSelection();
                        refreshList();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                if (checked){
                    selectedItems.add(position);
                    nr++;
                    listAdapter.setNewSelection(position, checked);
                }else{
                    selectedItems.remove(selectedItems.indexOf(position));
                    nr--;
                    listAdapter.removeSelection(position);
                }
                mode.setTitle(nr + " " + getString(R.string.action_mode_selected));
            }
        });

        globalsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                view.setSelected(true);
                globalsList.setItemChecked(position, !listAdapter.isPositionChecked(position));
                return false;
            }
        });

        globalsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editClicked(superglobals.get(position).getBundle());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onColorPicked(DialogFragment fragment) {
        refreshList();
    }

    @Override
    public void onDone(DialogFragment fragment) {
        refreshList();
    }

    @Override
    public void onCancel(DialogFragment fragment) {

    }

    private void refreshList(){
        superglobals = db.getSuperglobals();
        listAdapter = new GlobalListAdapter(this,superglobals);
        globalsList.setAdapter(listAdapter);
    }

    @Override
    public void editClicked(Bundle args) {
        switch (args.getString(SuperglobalsContract.EXTRA_TYPE)){
            case Superglobal.TYPE_COLOR:
                ColorDialogFragment colorDialog = new ColorDialogFragment();
                colorDialog.setArguments(args);
                colorDialog.show(getFragmentManager(),"dialog");
                break;
            case Superglobal.TYPE_NUMBER:
                DefaultDialogFragment defaultDialog = new DefaultDialogFragment();
                defaultDialog.setArguments(args);
                defaultDialog.show(getFragmentManager(),"dialog");
                break;
        }
    }
}
