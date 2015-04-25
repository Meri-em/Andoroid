package com.unisofia.fmi.puzzles;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

public class UpdateTimerRunnable implements Runnable{
    public boolean active = true;
    public long startTime;

    private Handler handler;
    private long timeInMilliseconds = 0L;
    public long updatedTime = 0L;
    private long timeSwapBuff = 0L;
    private TextView timerTextView;

    public UpdateTimerRunnable(long startTime, Handler handler, TextView timerTextView) {
        this.startTime = startTime;
        this.handler = handler;
        this.timerTextView = timerTextView;
    }

    @Override public void run() {
        if (!active) {
            return;
        }
        timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
        updatedTime = timeSwapBuff + timeInMilliseconds;

        int seconds =(int) updatedTime / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        timerTextView. setText("Time: " + minutes + ":"
                + String.format("%02d", seconds));

        handler.postDelayed(this, 1000);
    }



}
