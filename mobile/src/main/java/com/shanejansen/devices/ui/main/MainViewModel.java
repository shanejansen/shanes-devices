package com.shanejansen.devices.ui.main;

import com.shanejansen.devices.data.DataManager;
import com.shanejansen.devices.data.models.Device;
import com.shanejansen.devices.ui.common.mvp.BaseViewModel;

import java.util.List;

/**
 * Created by Shane Jansen on 5/24/16.
 */
public class MainViewModel extends BaseViewModel<MvpMain.PresenterForModelOps> implements MvpMain.ModelForPresenterOps {
    private List<Device> mDevices;

    public MainViewModel() {}

    @Override
    public void loadDevices() {
        DataManager.refreshDevices(new DataManager.NetworkInf<List<Device>>() {
            @Override
            public void onCompleted(List<Device> result) {
                mDevices = result;
                getPresenter().onLoadedDevices(mDevices);
            }
        });
    }

    @Override
    public Device getDevice(int index) {
        try {
            return mDevices.get(index);
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public int getDeviceCount() {
        try {
            return mDevices.size();
        }
        catch (NullPointerException e) {
            return -1;
        }
    }
}
