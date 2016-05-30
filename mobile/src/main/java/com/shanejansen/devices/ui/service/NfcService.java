package com.shanejansen.devices.ui.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.shanejansen.devices.R;
import com.shanejansen.devices.data.DataManager;
import com.shanejansen.devices.data.models.Device;
import com.shanejansen.devices.ui.main.MainActivity;

import java.io.IOException;
import java.util.List;

/**
 * Created by Shane Jansen on 5/30/16.
 */
public class NfcService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private static final int SERVICE_ID = 1;
    private static final String ACTION_CANCEL_PRESSED = "com.shanejansen.devices.NfcService.ACTION_CANCEL_PRESSED";

    private NotificationManager mNotificationManager;
    private BroadcastReceiver mCancelReceiver;
    private PowerManager.WakeLock mWakeLock;
    private List<Device> mDevices;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startOngoingNotification();

        // Set the wakelock
        PowerManager pm = (PowerManager) NfcService.this.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyLock");
        mWakeLock.acquire();

        // Get the devices
        DataManager.refreshDevices(new DataManager.NetworkInf<List<Device>>() {
            @Override
            public void onCompleted(List<Device> result) {
                mDevices = result;
                // Turn on each device
                for (Device device : mDevices) {
                    DataManager.activateDevice(device.getPin(), true, new DataManager.NetworkInf<String>() {
                        @Override
                        public void onCompleted(String result) {
                            //
                        }
                    });
                }
            }
        });

        // Check that the NFC tag is still available
        Tag tag = intent.getParcelableExtra("nfcTag");
        final Ndef ndefTag = Ndef.get(tag);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ndefTag.connect();
                    while (true) {
                        Thread.sleep(5000);
                        ndefTag.getNdefMessage();
                    }
                } catch (InterruptedException | FormatException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    stopSelf();
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // Turn off each device
        for (Device device : mDevices) {
            DataManager.activateDevice(device.getPin(), false, new DataManager.NetworkInf<String>() {
                @Override
                public void onCompleted(String result) {
                    //
                }
            });
        }

        mWakeLock.release();
        unregisterReceiver(mCancelReceiver);
        super.onDestroy();
    }

    private void startOngoingNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_connected));
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setOngoing(true);

        // Set and register broadcast receivers for clicking notification buttons
        mCancelReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                stopSelf();
            }
        };
        registerReceiver(mCancelReceiver, new IntentFilter(ACTION_CANCEL_PRESSED));

        // Create intents for pressing notification buttons
        Intent disconnectedReceive = new Intent(ACTION_CANCEL_PRESSED);
        PendingIntent pendingIntentMotion = PendingIntent.getBroadcast(this, 123, disconnectedReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        // Add the buttons and intents to notification
        mBuilder.addAction(android.R.drawable.ic_dialog_alert, "Cancel", pendingIntentMotion);

        // Build and show notification
        Notification notification = mBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID, notification);
        
        startForeground(SERVICE_ID, notification);
    }
}
