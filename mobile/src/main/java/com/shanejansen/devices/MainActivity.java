package com.shanejansen.devices;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.shanejansen.devices.R;
import com.shanejansen.devices.common.BaseActivity;
import com.shanejansen.devices.common.DataManager;
import com.shanejansen.devices.main.MainFragment;

public class MainActivity extends BaseActivity {
    // Constants
    public static final String TAG = "main";
    private static final String MAIN_FRAGMENT = "main_fragment";
    private static final int MAIN_CONTAINER = R.id.flMainContainer;

    // Data
    private MainFragment mMainFragment;
    private FragmentManager mFragmentManager;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();

        // The app could have been launched via NFC. In this case,
        // we want to start a background service.
        Intent intent = getIntent();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            Context context = getApplicationContext(); // Use this context for starting service
            finish();
            return;
        }

        // See if any fragments already exist
        mMainFragment = (MainFragment) mFragmentManager
                .findFragmentByTag(MainFragment.class.getName());

        if (savedInstanceState == null) {
            // The initial fragment(s) need to be created and added
            mMainFragment = new MainFragment();
            addFragment(mMainFragment, MAIN_CONTAINER, false);
        }


        if (mGoogleApiClient == null) {
            DataManager.setupGoogleApiClient(this, new DataManager.NetworkInf<GoogleApiClient>() {
                @Override
                public void onCompleted(GoogleApiClient result) {
                    mGoogleApiClient = result;
                }
            });
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void setActionBarTitle() {
        if (mFragmentManager != null) {
            assert getSupportActionBar() != null;
            getSupportActionBar().setTitle("Shane\'s Devices");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null) mGoogleApiClient.disconnect();
    }
}
