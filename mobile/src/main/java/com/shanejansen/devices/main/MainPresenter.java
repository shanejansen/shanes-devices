package com.shanejansen.devices.main;

import android.content.Context;

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
    public void unbindView(boolean isConfigurationChange) {
        mView = null;
        mModel.unbindPresenter(isConfigurationChange);
        if (!isConfigurationChange) {
            // Nulls Model when the Activity destruction is permanent
            mModel = null;
        }
    }

    @Override
    public void bindView(MvpMain.ViewForPresenterOps view) {
        mView = new WeakReference<>(view);
    }

    private MvpMain.ViewForPresenterOps getView() throws NullPointerException {
        if (mView != null) return mView.get();
        else throw new NullPointerException("View in unavailable");
    }

    /**
     * Only called once
     * @param model
     */
    @Override
    public void bindModel(MvpMain.ModelForPresenterOps model) {
        mModel = model;
        // TODO: loadData();
    }
}
