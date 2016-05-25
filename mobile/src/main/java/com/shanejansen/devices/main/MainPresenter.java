package com.shanejansen.devices.main;

import android.content.Context;

import com.shanejansen.devices.common.mvp.BaseMvp;

import java.lang.ref.WeakReference;

/**
 * Created by Shane Jansen on 5/24/16.
 */
public class MainPresenter implements MvpMain.PresenterForViewOps, MvpMain.PresenterForModelOps {
    // Data
    private WeakReference<MvpMain.ViewForPresenterOps> mView;
    private MvpMain.ModelForPresenterOps mModel;

    public MainPresenter(MvpMain.ViewForPresenterOps view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public void clickedRefresh() {
        // TODO
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        mView = null;
        mModel.onDestroy(isChangingConfiguration);
        if (!isChangingConfiguration) {
            // Nulls Model when the Activity destruction is permanent
            mModel = null;
        }
    }

    @Override
    public void setView(MvpMain.ViewForPresenterOps view) {
        mView = new WeakReference<>(view);
    }

    private MvpMain.ViewForPresenterOps getView() throws NullPointerException {
        if (mView != null) return mView.get();
        else throw new NullPointerException("View in unavailable");
    }

    public void setModel(MvpMain.ModelForPresenterOps model) {
        mModel = model;
        // TODO
    }
}
