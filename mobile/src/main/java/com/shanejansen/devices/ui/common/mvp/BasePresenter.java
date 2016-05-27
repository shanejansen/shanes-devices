package com.shanejansen.devices.ui.common.mvp;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by Shane Jansen on 5/26/16.
 *
 * Base Presenter for the MVP architecture.
 */
public abstract class BasePresenter <V, M> {
    private boolean mInitialized;
    private WeakReference<V> mView;
    private M mViewModel;

    public BasePresenter() {
        mInitialized = false;
    }

    /**
     * Called only once after the Model is bound. Will not be called again even
     * if the Presenter is restored.
     */
    protected abstract void initView();

    /**
     * Called each time the view is ready to be updated. Should only be called once
     * the View and ViewModel are bound.
     */
    protected abstract void updateView();

    public void viewReady() {
        if (mInitialized) updateView();
        else {
            initView();
            mInitialized = true;
        }
    }

    public Context getAppContext() {
        try {
            return ((BaseView) getView()).getAppContext();
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public Activity getActivityContext() {
        try {
            return ((BaseView) getView()).getActivityContext();
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public void unbind(boolean isConfigurationChange) {
        mView = null;
        ((BaseViewModel) mViewModel).unbindPresenter(isConfigurationChange);
        if (!isConfigurationChange) mViewModel = null;
    }

    public void bindView(V view) {
        mView = new WeakReference<>(view);
    }

    protected V getView() throws NullPointerException {
        if (mView != null) return mView.get();
        else throw new NullPointerException("View in unavailable");
    }

    public void bindModel(M model) {
        mViewModel = model;
    }

    protected M getViewModel() throws NullPointerException {
        if (mViewModel != null) return mViewModel;
        else throw new NullPointerException("ViewModel is unavailable");
    }
}
