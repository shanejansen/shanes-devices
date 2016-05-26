package com.shanejansen.devices.common.mvp;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by Shane Jansen on 5/26/16.
 * Base Presenter for the MVP architecture.
 */
public abstract class BasePresenter <V extends BaseView, M extends BaseModel> {
    private WeakReference<V> mView;
    private M mModel;

    /**
     * Called only once after the Model is bound. Will not be called again even
     * if the Presenter is restored.
     */
    protected abstract void init();

    public Context getAppContext() {
        try {
            return getView().getAppContext();
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public Activity getActivityContext() {
        try {
            return getView().getActivityContext();
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public void unbind(boolean isConfigurationChange) {
        mView = null;
        mModel.unbindPresenter(isConfigurationChange);
        if (!isConfigurationChange) mModel = null;
    }

    public void bindView(V view) {
        mView = new WeakReference<>(view);
    }

    protected V getView() throws NullPointerException {
        if (mView != null) return mView.get();
        else throw new NullPointerException("View in unavailable");
    }

    public void bindModel(M model) {
        mModel = model;
        init();
    }

    protected M getModel() throws NullPointerException {
        if (mModel != null) return mModel;
        else throw new NullPointerException("Model is unavailable");
    }
}
