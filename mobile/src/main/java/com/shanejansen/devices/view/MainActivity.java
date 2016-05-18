package com.shanejansen.devices.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.common.api.GoogleApiClient;
import com.shanejansen.devices.R;
import com.shanejansen.devices.data.DataManager;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    // Constants
    public static final String TAG = "main";
    private static final String MAIN_FRAGMENT = "main_fragment";
    private static final int MAIN_CONTAINER = R.id.flMainContainer;

    // Data
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        DataManager.setupGoogleApiClient(this, new DataManager.NetworkInf<GoogleApiClient>() {
            @Override
            public void onCompleted(GoogleApiClient result) {
                mGoogleApiClient = result;
            }
        });
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT);
        if (fragment == null) fragment = new MainFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(MAIN_CONTAINER, fragment, MAIN_FRAGMENT);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }
}
