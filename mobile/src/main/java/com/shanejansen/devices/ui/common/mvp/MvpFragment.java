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
            mPresenter.bindModel(model);
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

    protected P getPresenter() {
        return mPresenter;
    }
}
