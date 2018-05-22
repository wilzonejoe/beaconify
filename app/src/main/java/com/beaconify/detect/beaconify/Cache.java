package com.beaconify.detect.beaconify;

import android.content.Context;

import java.io.File;
import java.io.IOException;

public class Cache {
    public static String token = "";

    public static boolean clear (Context context) {
        token = new String();
        File directory = context.getFilesDir();
        File file = new File(directory, "UserInfo.json");
        return file.delete();
    }
}
