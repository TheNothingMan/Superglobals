package com.stegabach.superglobals;

import android.provider.BaseColumns;

/**
 * Created by basti on 22.05.2016.
 */
public final class SuperglobalsContract {
    public SuperglobalsContract(){}

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_VALUE = "value";
    public static final String EXTRA_COLOR_INT = "colorInt";

    //Default Color is black with 100% opacity
    public static final int DEFAULT_COLOR = -16777216;

    public static final String EXTRA_ID = "id";

    public static abstract class GlobalEntry implements BaseColumns{
        public static final String TABLE_NAME = "globals";
        public static final String COLUMN_NAME_GLOBAL_ID = "name";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_VALUE = "value";
    }
}
