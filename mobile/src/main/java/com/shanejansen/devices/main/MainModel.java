package com.shanejansen.devices.main;

import com.shanejansen.devices.common.DataManager;
import com.shanejansen.devices.common.mvp.BaseModel;
import com.shanejansen.devices.models.Device;

import java.util.List;

/**
 * Created by Shane Jansen on 5/24/16.
 */
public class MainModel implements MvpMain.ModelForPresenterOps {
    // Data
    private MvpMain.PresenterForModelOps mPresenter;
    private List<Device> mDevices;

    public MainModel(MvpMain.PresenterForModelOps presenter) {
        mPresenter = presenter;
    }

    @Override
    public void loadData(final BaseModel.Callback callback) {
        DataManager.refreshDevices(new DataManager.NetworkInf<List<Device>>() {
            @Override
            public void onCompleted(List<Device> result) {
                mDevices = result;
                callback.loadSuccess();
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

    @Override
    public void unbindPresenter(boolean isConfigurationChange) {
        if (!isConfigurationChange) {
            mPresenter = null;
        }
    }
}
