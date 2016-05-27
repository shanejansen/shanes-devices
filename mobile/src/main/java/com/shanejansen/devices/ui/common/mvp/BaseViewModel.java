package com.shanejansen.devices.ui.common.mvp;

/**
 * Created by Shane Jansen on 5/24/16.
 *
 * Base ViewModel for the MVP architecture.
 */
public abstract class BaseViewModel<P> {
    private P mPresenter;

    public void bindPresenter(P presenter) {
        mPresenter = presenter;
    }

    public void unbindPresenter(boolean isConfigurationChange) {
        if (!isConfigurationChange) mPresenter = null;
    }

    protected P getPresenter() throws NullPointerException {
        if (mPresenter != null) return mPresenter;
        else throw new NullPointerException("Presenter is unavailable");
    }
}
