package com.ddopik.linktask.utilites;

import android.os.Environment;

/**
 * Created by ddopik..@_@
 */
public class DiskHelper {
    //    Return true if External storage is mounted for read and write
    public boolean isSdCardMounted() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);

    }
}
