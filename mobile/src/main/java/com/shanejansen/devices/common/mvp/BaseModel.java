package com.shanejansen.devices.common.mvp;

/**
 * Created by Shane Jansen on 5/24/16.
 *
 * Base Model for the MVP architecture.
 */
public abstract class BaseModel <P extends BasePresenter> {
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

    /**
     * Used as callbacks by the Presenter when the model is loading data.
     */
    public interface Callback {
        void loadSuccess();
        void loadFailure(String message);
    }
}
