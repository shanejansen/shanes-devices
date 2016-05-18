package com.shanejansen.devices.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shanejansen.devices.R;
import com.shanejansen.devices.data.DataManager;
import com.shanejansen.devices.data.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public class MainFragment extends ListFragment {
    // Constants
    public static final String WEAR_GET_STATE = "get_state";
    public static final String WEAR_SET_STATE = "set_state";

    // Data
    private DevicesAdapter mDevicesAdapter;
    private List<Device> mDevices;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDevices = new ArrayList<>();
        mDevicesAdapter = new DevicesAdapter(getActivity(), mDevices, new DevicesAdapter.DevicesAdapterInterface() {
            @Override
            public void switchToggled(int index, boolean isChecked) {
                Device device = mDevices.get(index);
                device.setIsOn(isChecked);
                activateDevice(device, isChecked);
            }
        });
        setHasOptionsMenu(true);
        setListAdapter(mDevicesAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshDevices();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                refreshDevices();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void refreshDevices() {
        setListShown(false);
        DataManager.refreshDevices(new DataManager.NetworkInf<List<Device>>() {
            @Override
            public void onCompleted(List<Device> result) {
                if (result == null) {
                    Toast.makeText(getContext().getApplicationContext(),
                            "Could not get list of devices.", Toast.LENGTH_LONG).show();
                    setListShown(true);
                }
                else {
                    setListShown(true);
                    mDevices.clear();
                    mDevices.addAll(result);
                    mDevicesAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void activateDevice(Device device, boolean state) {
        DataManager.activateDevice(device.getPin(), state,
                new DataManager.NetworkInf<String>() {
                    @Override
                    public void onCompleted(String result) {
                        if (result == null) {
                            Toast.makeText(getContext().getApplicationContext(),
                                    "Could not activate device.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
