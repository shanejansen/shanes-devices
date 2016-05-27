package com.shanejansen.devices.ui.common.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by Shane Jansen on 3/25/16.
 */
public interface TransactionInterface {
    boolean isTablet();
    void addFragment(Fragment fragment, int containerId, boolean addToBackStack);
    void removeCurrentFragment();
}
