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
    private WeakReference<V> mView;
    private M mViewModel;

    /**
     * Called only once after the Model is bound. Will not be called again even
     * if the Presenter is restored.
     */
    protected abstract void init();

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
        init();
    }

    protected M getViewModel() throws NullPointerException {
        if (mViewModel != null) return mViewModel;
        else throw new NullPointerException("ViewModel is unavailable");
    }
}
