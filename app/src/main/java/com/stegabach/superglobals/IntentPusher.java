package com.stegabach.superglobals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by basti on 24.05.2016.
 */
public class IntentPusher {
    public static final String KUSTOM_ACTION = "org.kustom.action.SEND_VAR";
    public static final String KUSTOM_ACTION_EXT_NAME = "org.kustom.action.EXT_NAME";
    public static final String KUSTOM_ACTION_VAR_NAME = "org.kustom.action.VAR_NAME";
    public static final String KUSTOM_ACTION_VAR_VALUE = "org.kustom.action.VAR_VALUE";
    public static final String KUSTOM_ACTION_VAR_NAME_ARRAY = "org.kustom.action.VAR_NAME_ARRAY";
    public static final String KUSTOM_ACTION_VAR_VALUE_ARRAY = "org.kustom.action.VAR_VALUE_ARRAY";

    /**
     * Send broadcasts to ZooperWidget and KustomWidget/LWP Apps.
     * @param context - context to fire the intent
     * @param name - the key
     * @param value - value for key
     */
    public static void fireIntents(Context context, String name, String value){
        //Kustom section
        Intent intent = new Intent(KUSTOM_ACTION);
        intent.putExtra(KUSTOM_ACTION_EXT_NAME, "sglobals");
        intent.putExtra(KUSTOM_ACTION_VAR_NAME, name);
        intent.putExtra(KUSTOM_ACTION_VAR_VALUE, value);
        context.sendBroadcast(intent);

        //Zooper section
        intent = new Intent("org.zooper.zw.action.TASKERVAR");
        Bundle bundle = new Bundle();
        bundle.putInt("org.zooper.zw.tasker.var.extra.INT_VERSION_CODE", 1);
        bundle.putString("org.zooper.zw.tasker.var.extra.STRING_VAR", name);
        bundle.putString("org.zooper.zw.tasker.var.extra.STRING_TEXT", value);
        intent.putExtra("org.zooper.zw.tasker.var.extra.BUNDLE", bundle);
        context.sendBroadcast(intent);
    }
}
