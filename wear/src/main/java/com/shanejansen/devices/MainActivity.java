package com.shanejansen.devices;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends WearableActivity implements MessageApi.MessageListener {
    // Constants
    public static final String TAG = "main";
    public static final String WEAR_GET_STATE = "get_state";
    public static final String WEAR_SET_STATE = "set_state";

    // Instances
    private GoogleApiClient mGoogleApiClient;
    private DevicesAdapter mDevicesAdapter;
    private ProgressBar pbLoading;

    // Data
    private List<Device> mDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGoogleApiClient();
        setContentView(R.layout.activity_main);
        mDevices = new ArrayList<>();
        mDevicesAdapter = new DevicesAdapter(this, mDevices, new DevicesAdapter.DevicesAdapterInterface() {
            @Override
            public void switchToggled(int index, boolean isChecked) {
                Device device = mDevices.get(index);
                device.setIsOn(isChecked);
                sendMobileMessage(WEAR_SET_STATE, device.getPin() + " " + isChecked);
            }
        });
        WearableListView lvDevices = (WearableListView) findViewById(R.id.lvDevices);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        lvDevices.setAdapter(mDevicesAdapter);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        pbLoading.setVisibility(View.GONE);
        if (messageEvent.getPath().equalsIgnoreCase(WEAR_GET_STATE)) {
            if (messageEvent.getData() != null) {
                String json = new String(messageEvent.getData());
                Gson gson = new Gson();
                List<Device> devices = gson.fromJson(json, new TypeToken<ArrayList<Device>>() {
                }.getType());
                mDevices.clear();
                mDevices.addAll(devices);
                mDevicesAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(this, "Could not get list of devices.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sendMobileMessage(final String path, final String payload) {
        pbLoading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodeResult = Wearable.NodeApi
                        .getConnectedNodes(mGoogleApiClient)
                        .await();
                for (Node node: nodeResult.getNodes()) {
                    MessageApi.SendMessageResult messageResult = Wearable.MessageApi
                            .sendMessage(mGoogleApiClient, node.getId(), path, payload.getBytes())
                            .await();
                }
            }
        }).start();
    }

    private void setupGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d(TAG, "onConnected: " + bundle);
                        Wearable.MessageApi.addListener(mGoogleApiClient, MainActivity.this);
                        sendMobileMessage(WEAR_GET_STATE, "");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "onConnectionSuspended: " + i);
                    }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }
}
