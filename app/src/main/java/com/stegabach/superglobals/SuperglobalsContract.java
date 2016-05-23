package com.stegabach.superglobals;

import android.provider.BaseColumns;

/**
 * Created by basti on 22.05.2016.
 */
public final class SuperglobalsContract {
    public SuperglobalsContract(){}

    public static abstract class GlobalEntry implements BaseColumns{
        public static final String TABLE_NAME = "globals";
        public static final String COLUMN_NAME_GLOBAL_ID = "globalId";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_VALUE = "value";
    }
}
