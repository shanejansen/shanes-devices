package com.shanejansen.devices.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.shanejansen.devices.R;
import com.shanejansen.devices.common.StateMaintainer;
import com.shanejansen.devices.common.fragments.BaseFragment;
import com.shanejansen.devices.models.Device;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public class MainFragment extends BaseFragment implements MvpMain.ViewForPresenterOps {
    // Constants
    public static final String WEAR_GET_STATE = "get_state";
    public static final String WEAR_SET_STATE = "set_state";

    // Data
    private MvpMain.PresenterForViewOps mPresenter;
    private StateMaintainer mStateMaintainer;

    // Views
    @Bind(R.id.rvList) RecyclerView mRvList;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*mDevices = new ArrayList<>();
        mDevicesAdapter = new DevicesAdapter(getActivity(), mDevices, new DevicesAdapter.DevicesAdapterInterface() {
            @Override
            public void switchToggled(int index, boolean isChecked) {
                Device device = mDevices.get(index);
                device.setIsOn(isChecked);
                activateDevice(device, isChecked);
            }
        });
        setHasOptionsMenu(true);
        setListAdapter(mDevicesAdapter);*/

        mStateMaintainer = new StateMaintainer(getChildFragmentManager(), MainFragment.class.getName());
        setupMvp();
    }

    @Override
    protected void onViewInflated(View v, Bundle savedInstanceState) {
        super.onViewInflated(v, savedInstanceState);
        //refreshDevices();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //refreshDevices();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    /*private void refreshDevices() {
        setListShown(false);
        DataManager.refreshDevices(new DataManager.NetworkInf<List<Device>>() {
            @Override
            public void onCompleted(List<Device> result) {
                if (result == null) {
                    Toast.makeText(getContext().getApplicationContext(),
                            "Could not get list of devices.", Toast.LENGTH_LONG).show();
                    setListShown(true);
                }
                else {
                    setListShown(true);
                    mDevices.clear();
                    mDevices.addAll(result);
                    mDevicesAdapter.notifyDataSetChanged();
                }
            }
        });
    }*/

    /*private void activateDevice(Device device, boolean state) {
        DataManager.activateDevice(device.getPin(), state,
                new DataManager.NetworkInf<String>() {
                    @Override
                    public void onCompleted(String result) {
                        if (result == null) {
                            Toast.makeText(getContext().getApplicationContext(),
                                    "Could not activate device.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/

    // TODO

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void notifyDataSetChanged() {

    }

    @Override
    public Context getAppContext() {
        return null;
    }

    @Override
    public Context getActivityContext() {
        return null;
    }

    private void setupMvp() {
        if (mStateMaintainer.firstTimeIn()) {
            MainPresenter presenter = new MainPresenter(this);
            MainModel model = new MainModel(presenter);
            presenter.setModel(model);
            mStateMaintainer.put(presenter);
            mStateMaintainer.put(model);
            mPresenter = presenter;
        }
        else {
            mPresenter = mStateMaintainer.get(MainPresenter.class.getName());
            mPresenter.setView(this);
        }
    }
}
