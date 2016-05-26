package com.shanejansen.devices.main;

import com.shanejansen.devices.common.mvp.BasePresenter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shane Jansen on 5/24/16.
 */
public class MainPresenter extends BasePresenter<MvpMain.ViewForPresenterOps, MainModel> implements MvpMain.PresenterForViewOps, MvpMain.PresenterForModelOps {
    // Data
    private int mTest;

    public MainPresenter() {}

    @Override
    protected void init() {
        getView().showToast("Inited");
    }

    @Override
    public void clickedRefresh() {
        mTest = 0;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivityContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTest++;
                        getView().showToast("Refresh clicked: " + mTest);
                    }
                });
            }
        }, 0, 5000);
    }
}
