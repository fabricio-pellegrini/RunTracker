package com.pellegrini.runtracker;

import java.util.Date;

/**
 * Created by Fabricio on 20/10/13.
 */
public class Run {

    private Date mStartDate;

    public Run(){
        mStartDate = new Date();
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date pStartDate) {
        mStartDate = pStartDate;
    }

    public int getDurationSeconds(long pEndMillis) {
        return (int) ((pEndMillis - mStartDate.getTime())/1000);
    }

    public static String formatDuratio(int pDurationSeconds) {

        int lSeconds = pDurationSeconds % 60;
        int lMinutes = ((pDurationSeconds - lSeconds) / 60) % 60;
        int lHours = (pDurationSeconds - (lMinutes * 60) - lSeconds) / 3600;

        return String.format("%02d:%02d:%02d", lHours, lMinutes, lSeconds);


    }
}
