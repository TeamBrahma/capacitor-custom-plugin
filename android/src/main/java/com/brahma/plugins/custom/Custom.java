package com.brahma.plugins.custom;

import android.util.Log;

public class Custom {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
