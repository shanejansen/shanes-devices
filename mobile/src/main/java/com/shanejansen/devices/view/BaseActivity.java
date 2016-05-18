package com.shanejansen.devices.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.shanejansen.devices.R;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    // Data
    private Toolbar mToolbar;

    protected abstract int getLayoutResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            setActionBarNavigateUp();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        for(int i=0; i<getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Checks if the back arrow in the action bar should
     * be displayed and enables it if is should be.
     */
    protected void setActionBarNavigateUp() {
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

    /**
     * Sets the title for the actionbar.
     * @param title Desired title.
     */
    protected void setActionBarTitle(String title) {
        if (mToolbar != null) {
            assert getSupportActionBar() != null;
            getSupportActionBar().setTitle(title);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
