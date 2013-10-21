package com.pellegrini.runtracker.singleton;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/**
 * Created by Fabricio on 20/10/13.
 */
public class RunManager {

    private static final String TAG = "RunManager";

    public static final String ACTION_LOCATION = "com.pellegrini.runtracker.singleton.ACTION_LOCATION";

    private static RunManager sRunManager;

    private Context mAppContext;

    private LocationManager mLocationManager;

    private RunManager(Context pAppContext){
        mAppContext = pAppContext;
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public static RunManager get(Context pContext){
        if(sRunManager == null)
            sRunManager = new RunManager(pContext.getApplicationContext());
        return sRunManager;
    }

    private PendingIntent getLocationPendingIntent(boolean pShouldCreate){
        Intent lBroadcastIntent = new Intent(ACTION_LOCATION);
        int lFlags = pShouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;

        return PendingIntent.getBroadcast(mAppContext, 0, lBroadcastIntent, lFlags);
    }

    public void startLocationUpdates(){
        String lProvider = LocationManager.GPS_PROVIDER;

        PendingIntent lPendingIntent = getLocationPendingIntent(true);

        mLocationManager.requestLocationUpdates(lProvider, 0, 0, lPendingIntent);
    }

    public void stopLocationUpdates(){

        PendingIntent lPendingIntent = getLocationPendingIntent(false);

        if(lPendingIntent != null){
            mLocationManager.removeUpdates(lPendingIntent);
            lPendingIntent.cancel();
        }
    }

    public boolean isTracking(){
        return getLocationPendingIntent(false) != null;
    }
}
