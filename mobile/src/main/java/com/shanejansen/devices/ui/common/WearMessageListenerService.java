package com.shanejansen.devices.ui.common;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;
import com.shanejansen.devices.data.DataManager;
import com.shanejansen.devices.data.models.Device;
import com.shanejansen.devices.ui.main.MainActivity;
import com.shanejansen.devices.ui.main.MainFragment;

import java.util.List;

/**
 * Created by Shane Jansen on 3/10/16.
 */
public class WearMessageListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(MainActivity.TAG, "Message received.");
        switch (messageEvent.getPath()) {
            case MainFragment.WEAR_GET_STATE:
                getState();
                break;
            case MainFragment.WEAR_SET_STATE:
                String[] dataChunks = new String(messageEvent.getData()).split(" ");
                int pin = Integer.parseInt(dataChunks[0]);
                boolean state = Boolean.parseBoolean(dataChunks[1]);
                setState(pin, state);
                break;
        }
    }

    private void getState() {
        DataManager.refreshDevices(new DataManager.NetworkInf<List<Device>>() {
            @Override
            public void onCompleted(final List<Device> devices) {
                // Devices refreshed
                DataManager.setupGoogleApiClient(WearMessageListenerService.this,
                        new DataManager.NetworkInf<GoogleApiClient>() {
                            @Override
                            public void onCompleted(GoogleApiClient googleApiClient) {
                                // Google API connected
                                Gson gson = new Gson();
                                String json = gson.toJson(devices);
                                DataManager.sendWearMessage(googleApiClient, MainFragment.WEAR_GET_STATE,
                                        json);
                            }
                        });
            }
        });
    }

    private void setState(int pin, boolean state) {
        Log.d(MainActivity.TAG, "Setting state.");
        DataManager.activateDevice(pin, state, new DataManager.NetworkInf<String>() {
            @Override
            public void onCompleted(String result) {
                // State set
                DataManager.setupGoogleApiClient(WearMessageListenerService.this,
                        new DataManager.NetworkInf<GoogleApiClient>() {
                            @Override
                            public void onCompleted(GoogleApiClient googleApiClient) {
                                // Google API connected
                                DataManager.sendWearMessage(googleApiClient, MainFragment.WEAR_SET_STATE,
                                        "done");
                            }
                        });
            }
        });
    }
}
