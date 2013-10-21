package com.pellegrini.runtracker.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pellegrini.runtracker.LocationReceiver;
import com.pellegrini.runtracker.R;
import com.pellegrini.runtracker.Run;
import com.pellegrini.runtracker.singleton.RunManager;

/**
 * Created by Fabricio on 20/10/13.
 */
public class RunFragment  extends Fragment{

    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {

        @Override
        protected void onLocationRecieved(Context pContext, Location pLocation) {
            mLastLocation = pLocation;

            if(isVisible()){
                updateUI();
            }
        }

        @Override
        protected void onProviderEnabledChanged(boolean pEnabled) {

            int lToastText = pEnabled ? R.string.gps_enabled : R.string.gps_disabled;
            Toast.makeText(getActivity(), lToastText, Toast.LENGTH_LONG).show();
        }
    };


    private RunManager mRunManager;

    private Run mRun;
    private Location mLastLocation;

    private Button mStartButton, mStopButton;
    private TextView mStartedTextView, mLatitudeTextView, mLongitudeTextView, mAltitudeTextView, mDurationTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mRunManager = RunManager.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View lView = inflater.inflate(R.layout.fragment_run, container, false);

        mStartedTextView = (TextView) lView.findViewById(R.id.run_startedTextView);
        mLatitudeTextView = (TextView) lView.findViewById(R.id.run_latitudeTextView);
        mLongitudeTextView = (TextView) lView.findViewById(R.id.run_longitudeTextView);
        mAltitudeTextView = (TextView) lView.findViewById(R.id.run_altitudeTextView);
        mDurationTextView = (TextView) lView.findViewById(R.id.run_durationTextView);

        mStartButton = (Button) lView.findViewById(R.id.run_startButton);
        mStartButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRunManager.startLocationUpdates();
                mRun = new Run();
                updateUI();
            }
        });

        mStopButton = (Button) lView.findViewById(R.id.run_stopButton);
        mStopButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRunManager.stopLocationUpdates();
                updateUI();
            }
        });

        updateUI();

        return lView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver, new IntentFilter(RunManager.ACTION_LOCATION));
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }

    private void updateUI(){
        boolean lStarted = mRunManager.isTracking();

        if(mRun != null) {
            mStartedTextView.setText(mRun.getStartDate().toString());
        }

        int lDurationSeconds = 0;

        if(mRun != null && mLastLocation != null) {
            lDurationSeconds = mRun.getDurationSeconds(mLastLocation.getTime());

            mLatitudeTextView.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeTextView.setText(Double.toString(mLastLocation.getAltitude()));
        }

        mDurationTextView.setText(Run.formatDuratio(lDurationSeconds));

        mStartButton.setEnabled(!lStarted);
        mStopButton.setEnabled(lStarted);
    }
}

