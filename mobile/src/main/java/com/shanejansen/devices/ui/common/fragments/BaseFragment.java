package com.shanejansen.devices.ui.common.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Shane Jansen on 3/30/16.
 *
 * Ensures that the activity hosting this fragment
 * implements the correct interfaces. Handles Butterknife.
 */
public abstract class BaseFragment extends DialogFragment {
    // Data
    private TransactionInterface mTransactionInterface;

    protected abstract int getLayoutResource();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mTransactionInterface = (TransactionInterface) getActivity();
        }
        catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement TransactionInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, v);
        onViewInflated(v, savedInstanceState);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected void onViewInflated(View v, Bundle savedInstanceState) {}

    public TransactionInterface getTransactionInterface() {
        return mTransactionInterface;
    }
}
