package com.shanejansen.devices.ui.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.shanejansen.devices.R;
import com.shanejansen.devices.data.models.Device;
import com.shanejansen.devices.ui.common.fragments.BaseFragment;
import com.shanejansen.devices.ui.common.mvp.PresenterMaintainer;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public class MainFragment extends BaseFragment implements MvpMain.ViewForPresenterOps {
    // Constants
    public static final String WEAR_GET_STATE = "get_state";
    public static final String WEAR_SET_STATE = "set_state";

    // Data
    private MainPresenter mPresenter;
    private DevicesAdapter mDevicesAdapter;

    // Views
    @Bind(R.id.rvList) RecyclerView mRvList;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // TODO: abstract out?
        if (savedInstanceState == null) {
            mPresenter = new MainPresenter();
            MainViewModel model = new MainViewModel();
            mPresenter.bindView(this);
            mPresenter.bindModel(model);
            model.bindPresenter(mPresenter);
        }
        else {
            mPresenter = PresenterMaintainer.getInstance().restorePresenter(savedInstanceState);
            mPresenter.bindView(this);
        }
    }

    @Override
    protected void onViewInflated(View v, Bundle savedInstanceState) {
        super.onViewInflated(v, savedInstanceState);
        mDevicesAdapter = new DevicesAdapter(getActivity(), new DevicesAdapter.DevicesAdapterInterface() {
            @Override
            public void switchToggled(int index, boolean isChecked) {
                // TODO
                /*
                Device device = mDevices.get(index);
                device.setIsOn(isChecked);
                activateDevice(device, isChecked);
                 */
                mPresenter.toggledDeviceSwitch(index, isChecked);
            }
        });
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRvList.setAdapter(mDevicesAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: Abstract out
        PresenterMaintainer.getInstance().savePresenter(mPresenter, outState);
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
                mPresenter.clickedRefresh();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO: Abstract out
        mPresenter.unbind(getActivity().isChangingConfigurations());
    }

    /*private void refreshDevices() {
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
    }*/

    /*private void activateDevice(Device device, boolean state) {
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
    }*/

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void notifyDevicesChanged(List<Device> devices) {
        mDevicesAdapter.clearAndAddAll(devices);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getAppContext() {
        // TODO: Abstract out
        return getActivity().getApplicationContext();
    }

    @Override
    public Activity getActivityContext() {
        // TODO: Abstract out
        return getActivity();
    }
}
