package com.krish.silenceme.util;

import android.content.Context;
import android.widget.Toast;

public class CommonUtil {

    static Context context = null;

    public CommonUtil(Context context){
        this.context = context;
    }

    public static void showMessage(String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.show();
    }
}
