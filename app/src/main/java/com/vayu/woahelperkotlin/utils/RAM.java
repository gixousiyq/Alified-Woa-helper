package com.vayu.woahelperkotlin.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.vayu.woahelperkotlin.utils.MemoryUtils;

public class RAM {

    public long getTotalMemory(Context context) {
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        assert actManager != null;
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;
        return totalMemory;
    }

    public String getMemory(Context context) {
        String mem;
        mem = new MemoryUtils().bytesToHuman(getTotalMemory(context));
        return mem;
    }
}
