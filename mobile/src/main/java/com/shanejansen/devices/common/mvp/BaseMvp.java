package com.shanejansen.devices.common.mvp;

import android.content.Context;

import com.shanejansen.devices.main.MvpMain;

/**
 * Created by Shane Jansen on 5/24/16.
 */
public interface BaseMvp {
    /**
     * Implemented by the view. These are the operations
     * that are available for the presenter.
     */
    interface ViewForPresenterOps {
        Context getAppContext();
        Context getActivityContext();
    }

    /**
     * Implemented by the presenter. These are the operations
     * that are available for the view.
     */
    interface PresenterForViewOps {
        void onDestroy(boolean isChangingConfiguration);
    }

    /**
     * Implemented by the model. These are the operations
     * that are available for the presenter.
     */
    interface ModelForPresenterOps {
        void onDestroy(boolean isChangingConfiguration);
    }

    /**
     * Implemented by the presenter. These are the operations
     * that are available for the model.
     */
    interface PresenterForModelOps {
        Context getAppContext();
        Context getActivityContext();
    }
}
