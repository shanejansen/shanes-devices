package com.shanejansen.devices.ui.common.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanejansen.devices.ui.common.fragments.BaseFragment;

/**
 * Created by Shane Jansen on 5/27/16.
 */
public abstract class MvpFragment <M extends BaseViewModel, V extends BaseView, P extends BasePresenter> extends BaseFragment
        implements BaseView {
    private P mPresenter;
    private M mTestModel;

    protected abstract M getMvpModel();
    protected abstract V getMvpView();
    protected abstract P getMvpPresenter();

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mPresenter = getMvpPresenter();
            M model = getMvpModel();
            mPresenter.bindView(getMvpView());
            if (mTestModel == null) mPresenter.bindModel(model);
            else mPresenter.bindModel(mTestModel);
            model.bindPresenter(mPresenter);
        }
        else {
            mPresenter = PresenterMaintainer.getInstance().restorePresenter(savedInstanceState);
            mPresenter.bindView(getMvpView());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mPresenter.viewReady();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterMaintainer.getInstance().savePresenter(mPresenter, outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unbind(getActivity().isChangingConfigurations());
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    /**
     * Returns the presenter for this view.
     * @return
     */
    public P getPresenter() {
        return mPresenter;
    }

    /**
     * Used for unit testing only. Sets the ViewModel so
     * asynchronous methods can be overridden.
     * @param testModel
     */
    public void setTestModel(M testModel) {
        mTestModel = testModel;
    }
}
