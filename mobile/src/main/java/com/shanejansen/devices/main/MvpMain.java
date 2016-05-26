package com.shanejansen.devices.main;

import com.shanejansen.devices.common.mvp.BaseModel;
import com.shanejansen.devices.common.mvp.BaseMvp;
import com.shanejansen.devices.models.Device;

/**
 * Created by Shane Jansen on 5/24/16.
 */
public interface MvpMain {
    interface ViewForPresenterOps extends BaseMvp.ViewForPresenterOps {
        void showProgress();
        void hideProgress();
        void notifyDataSetChanged();
    }

    interface PresenterForViewOps extends BaseMvp.PresenterForViewOps {
        void clickedRefresh();
        void bindView(ViewForPresenterOps view);
        void bindModel(ModelForPresenterOps model);
    }

    interface ModelForPresenterOps extends BaseMvp.ModelForPresenterOps {
        void loadData(BaseModel.Callback callback);
        Device getDevice(int index);
        int getDeviceCount();
    }

    interface PresenterForModelOps extends BaseMvp.PresenterForModelOps {

    }
}
