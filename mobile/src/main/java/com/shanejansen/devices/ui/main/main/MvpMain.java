package com.shanejansen.devices.ui.main.main;

import com.shanejansen.devices.data.models.Device;
import com.shanejansen.devices.ui.common.mvp.BaseView;

import java.util.List;

/**
 * Created by Shane Jansen on 5/24/16.
 */
public interface MvpMain {
    /**
     * Implemented by the view. These are the operations
     * that are available for the presenter.
     */
    interface ViewForPresenterOps extends BaseView {
        void showProgress();
        void hideProgress();
        void notifyDevicesChanged(List<Device> devices);
        void showToast(String message);
    }

    /**
     * Implemented by the presenter. These are the operations
     * that are available for the view.
     */
    interface PresenterForViewOps {
        void clickedRefresh();
        void toggledDeviceSwitch(int index, boolean isChecked);
    }

    /**
     * Implemented by the model. These are the operations
     * that are available for the presenter.
     */
    interface ModelForPresenterOps {
        void loadDevices();
        void activateDevice(Device device, boolean isChecked);
        List<Device> getDevices();
        int getDeviceCount();
    }

    /**
     * Implemented by the presenter. These are the operations
     * that are available for the model.
     */
    interface PresenterForModelOps {
        void onLoadedDevices(List<Device> devices);
    }
}
