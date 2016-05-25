package com.shanejansen.devices.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by Shane Jansen on 5/25/16.
 */
public class StateMaintainer {
    // Data
    private final String mStateMaintainerTag;
    private final WeakReference<FragmentManager> mFragmentManager;
    private StateMngFragment mStateMngFragment;
    private boolean mIsRecreated;

    public StateMaintainer(FragmentManager fragmentManager, String stateMaintainerTag) {
        mFragmentManager = new WeakReference<>(fragmentManager);
        FragmentManager test = mFragmentManager.get();
        mStateMaintainerTag = stateMaintainerTag;
    }

    /**
     * Creates the Fragment responsible for maintaining the objects.
     * @return
     */
    public boolean firstTimeIn() {
        try {
            mStateMngFragment = (StateMngFragment)
                    mFragmentManager.get().findFragmentByTag(mStateMaintainerTag);

            if (mStateMngFragment == null) {
                mStateMngFragment = new StateMngFragment();
                mFragmentManager.get().beginTransaction()
                        .add(mStateMngFragment, mStateMaintainerTag)
                        .commit();
                mIsRecreated = false;
                return true;
            }
            else {
                mIsRecreated = true;
                return false;
            }
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Returns true if the current Activity was recreated at least
     * one time.
     * @return
     */
    public boolean wasRecreated() {
        return mIsRecreated;
    }

    public void put(String key, Object object) {
        mStateMngFragment.put(key, object);
    }

    public void put(Object object) {
        put(object.getClass().getName(), object);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key)  {
        return mStateMngFragment.get(key);
    }

    public boolean hasKey(String key) {
        return mStateMngFragment.get(key) != null;
    }

    public static class StateMngFragment extends Fragment {
        private HashMap<String, Object> mData = new HashMap<>();

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        public void put(String key, Object object) {
            mData.put(key, object);
        }

        public void put(Object object) {
            put(object.getClass().getName(), object);
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String key) {
            return (T) mData.get(key);
        }
    }
}
