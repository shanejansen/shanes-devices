package com.shanejansen.devices.ui.main;

import com.shanejansen.devices.data.models.Device;
import com.shanejansen.devices.ui.common.mvp.BasePresenter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shane Jansen on 5/24/16.
 */
public class MainPresenter extends BasePresenter<MvpMain.ViewForPresenterOps, MvpMain.ModelForPresenterOps>
        implements MvpMain.PresenterForViewOps, MvpMain.PresenterForModelOps {
    // Data
    private int mTest;

    public MainPresenter() {
        super();
        mTest = 0;
    }

    @Override
    protected void initView() {
        getView().showProgress();
        getViewModel().loadDevices();
    }

    @Override
    protected void updateView() {
        if (getViewModel().getDeviceCount() != -1) {
            getView().notifyDevicesChanged(getViewModel().getDevices());
        }
    }

    @Override
    public void clickedRefresh() {
        //startTestTimer();
        getView().showProgress();
        getViewModel().loadDevices();
    }

    @Override
    public void toggledDeviceSwitch(int index, boolean isChecked) {
        Device device = getViewModel().getDevices().get(index);
        device.setIsOn(isChecked);
        getViewModel().activateDevice(device, isChecked);
    }

    @Override
    public void onLoadedDevices(List<Device> devices) {
        getView().hideProgress();
        if (devices == null) {
            getView().showToast("Could not get the list of devices.");
        }
        else {
            getView().notifyDevicesChanged(devices);
        }
    }

    private void startTestTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivityContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTest++;
                        getView().showToast("Refresh clicked: " + mTest);
                    }
                });
            }
        }, 0, 5000);
    }
}
