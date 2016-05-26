package com.shanejansen.devices.main;

import com.shanejansen.devices.common.mvp.BaseModel;
import com.shanejansen.devices.common.mvp.BaseView;
import com.shanejansen.devices.models.Device;

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
        void notifyDataSetChanged();
        void showToast(String message);
    }

    /**
     * Implemented by the presenter. These are the operations
     * that are available for the view.
     */
    interface PresenterForViewOps {
        void clickedRefresh();
    }

    /**
     * Implemented by the model. These are the operations
     * that are available for the presenter.
     */
    interface ModelForPresenterOps {
        void loadData(BaseModel.Callback callback);
        Device getDevice(int index);
        int getDeviceCount();
    }

    /**
     * Implemented by the presenter. These are the operations
     * that are available for the model.
     */
    interface PresenterForModelOps {

    }
}
