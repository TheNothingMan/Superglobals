package com.stegabach.superglobals;

import android.graphics.Color;
import android.os.Bundle;

/**
 * Created by basti on 23.05.2016.
 */
public class Superglobal {
    public static final String TYPE_COLOR = "color";
    public static final String TYPE_NUMBER = "number";
    public static final String TYPE_SWITCH = "switch";
    private long id;
    private String name;
    private String type;
    private String value;

    /**
     * Generate a new global.
     * @param name
     * @param type
     * @param value
     */
    public Superglobal(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Superglobal(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Superglobal(long id, String name, String type, String value) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValueAsString() {
        return value;
    }

    public void setValueString(String value) {
        this.value = value;
    }

    /**
     * Get the color as int for direct use in ColorPanelView or ColorPickerView
     * @return int color
     */
    public int getColorValue(){
        return Color.parseColor(value);
    }

    public Bundle getBundle(){
        Bundle args = new Bundle();

        args.putString(SuperglobalsContract.EXTRA_NAME,name);
        args.putString(SuperglobalsContract.EXTRA_TYPE,type);
        args.putLong(SuperglobalsContract.EXTRA_ID,id);
        args.putString(SuperglobalsContract.EXTRA_VALUE,value);

        if (type.equals(TYPE_COLOR)){
            args.putInt(SuperglobalsContract.EXTRA_COLOR_INT,getColorValue());
        }

        return args;
    }
}