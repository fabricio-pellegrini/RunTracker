package com.pellegrini.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by Fabricio on 20/10/13.
 */
public class LocationReceiver extends BroadcastReceiver {

    private static final String TAG = "LocationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Location lLocation = (Location) intent
                .getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);

        if(lLocation != null){
            onLocationRecieved(context, lLocation);
            return;
        }

        if(intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)){
            boolean lEnabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
            onProviderEnabledChanged(lEnabled);
        }
    }

    protected void onLocationRecieved(Context pContext, Location pLocation){
        Log.d(TAG, this + "Got location from " + pLocation.getProvider() + ": " +
        pLocation.getLatitude() + ", " + pLocation.getLongitude());
    }

    protected void onProviderEnabledChanged(boolean pEnabled){
        Log.d(TAG, "Provider " + (pEnabled ? "enabled!" : "disabled!" ));
    }
}
