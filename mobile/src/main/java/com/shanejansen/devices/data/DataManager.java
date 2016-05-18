package com.shanejansen.devices.data;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shanejansen.devices.view.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shane Jansen on 3/10/16.
 */
public class DataManager {
    // Constants
    public static final String GET_STATE_ENDPOINT = "/get-state.php";
    public static final String SET_STATE_ENDPOINT = "/set-state.php";

    public static void refreshDevices(final NetworkInf<List<Device>> networkInf) {
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                String response = "";
                try {
                    URL url = new URL(Secret.BASE_URL + GET_STATE_ENDPOINT);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) response += line;
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                final List<Device> devices = gson.fromJson(response, new TypeToken<ArrayList<Device>>(){}.getType());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        networkInf.onCompleted(devices);
                    }
                });
            }
        }).start();
    }

    public static void activateDevice(final int pin, final boolean state,
                                      final NetworkInf<String> networkInf) {
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                String response = "";
                try {
                    URL url = new URL(Secret.BASE_URL + SET_STATE_ENDPOINT);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    String str = pin + " " + state;
                    byte[] outputBytes = str.getBytes("UTF-8");
                    OutputStream os = connection.getOutputStream();
                    os.write(outputBytes);
                    os.close();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) response += line;
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final String finalResponse = response;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        networkInf.onCompleted(finalResponse);
                    }
                });
            }
        }).start();
    }

    public static void setupGoogleApiClient(Context context, final NetworkInf<GoogleApiClient> networkInf) {
        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                if (networkInf != null) networkInf.onCompleted(googleApiClient);
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(MainActivity.TAG, "onConnectionSuspended: " + i);
            }
        });
        googleApiClient.connect();
    }

    public static void sendWearMessage(final GoogleApiClient googleApiClient, final String path,
                                       final String payload) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodeResult = Wearable.NodeApi
                        .getConnectedNodes(googleApiClient)
                        .await();
                for (Node node: nodeResult.getNodes()) {
                    MessageApi.SendMessageResult messageResult = Wearable.MessageApi
                            .sendMessage(googleApiClient, node.getId(), path, payload.getBytes())
                            .await();
                }
            }
        }).start();
    }

    public interface NetworkInf<T> {
        void onCompleted(T result);
    }
}
