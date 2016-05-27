package com.shanejansen.devices.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.shanejansen.devices.R;
import com.shanejansen.devices.ui.common.fragments.TransactionInterface;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements
        TransactionInterface, FragmentManager.OnBackStackChangedListener {
    // Data
    private Toolbar mToolbar;
    private boolean mIsTablet; // True if this device is a tablet (at least sw600dp)

    protected abstract int getLayoutResource();
    protected abstract void setActionBarTitle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        // Check if this is a tablet
        if (getResources().getBoolean(R.bool.isTablet)) mIsTablet = true;

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            setActionBarNavigation();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        setActionBarTitle();
    }

    @Override
    public boolean onSupportNavigateUp() {
        int numBack = getSupportFragmentManager().getBackStackEntryCount();
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (numBack == 0 && upIntent != null) finish();
        else if (numBack != 0) removeCurrentFragment();
        return true;
    }

    /* -------------- TransactionInterface Callbacks -------------- */
    @Override
    public boolean isTablet() {
        return mIsTablet;
    }

    @Override
    public void addFragment(Fragment fragment, int containerId, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment, fragment.getClass().getName());
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void removeCurrentFragment() {
        getSupportFragmentManager().popBackStack();
    }

    /* -------------- FragmentManager Callbacks -------------- */
    @Override
    public void onBackStackChanged() {
        setActionBarNavigation();
        setActionBarTitle();
    }

    /**
     * Checks if the back arrow in the action bar should
     * be displayed and enables it if is should be.
     */
    protected void setActionBarNavigation() {
        if (mToolbar != null) {
            int numBack = getSupportFragmentManager().getBackStackEntryCount();
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (numBack != 0 || upIntent != null) {
                assert getSupportActionBar() != null;
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
            else {
                assert getSupportActionBar() != null;
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }
    }
}
