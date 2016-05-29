package com.shanejansen.devices.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shanejansen.devices.R;
import com.shanejansen.devices.data.models.Device;
import com.shanejansen.devices.ui.common.mvp.MvpFragment;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public class MainFragment extends MvpFragment<MainViewModel, MvpMain.ViewForPresenterOps, MainPresenter>
        implements MvpMain.ViewForPresenterOps {
    // Constants
    public static final String WEAR_GET_STATE = "get_state";
    public static final String WEAR_SET_STATE = "set_state";

    // Data
    private DevicesAdapter mDevicesAdapter;

    // Views
    @Bind(R.id.pbLoading) ProgressBar mPbLoading;
    @Bind(R.id.rvList) RecyclerView mRvList;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main;
    }

    @Override
    protected MainViewModel getMvpModel() {
        return new MainViewModel();
    }

    @Override
    protected MvpMain.ViewForPresenterOps getMvpView() {
        return this;
    }

    @Override
    protected MainPresenter getMvpPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void onViewInflated(View v, Bundle savedInstanceState) {
        super.onViewInflated(v, savedInstanceState);
        mDevicesAdapter = new DevicesAdapter(getActivity(), new DevicesAdapter.DevicesAdapterInterface() {
            @Override
            public void switchToggled(int index, boolean isChecked) {
                getPresenter().toggledDeviceSwitch(index, isChecked);
            }
        });
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRvList.setAdapter(mDevicesAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getPresenter().clickedRefresh();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void showProgress() {
        mPbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void notifyDevicesChanged(List<Device> devices) {
        mDevicesAdapter.clearAndAddAllData(devices);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
