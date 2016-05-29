package com.shanejansen.devices.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.shanejansen.devices.BuildConfig;
import com.shanejansen.devices.R;
import com.shanejansen.devices.data.models.Device;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

/**
 * Created by Shane Jansen on 5/28/16.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainFragmentTest {
    private List<Device> mDevices;
    private MainFragment mMainFragment;
    private MainViewModel mMockMainViewModel;

    @Before
    public void setup() {
        // Test data
        mDevices = new ArrayList<>();
        mDevices.add(new Device());

        // Test view
        mMainFragment = new MainFragment();

        // Mock data
        mMockMainViewModel = Mockito.mock(MainViewModel.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                mMainFragment.getPresenter().onLoadedDevices(mDevices);
                return null;
            }
        }).when(mMockMainViewModel).loadDevices();
        when(mMockMainViewModel.getDevices()).thenReturn(mDevices);

        // Init
        mMainFragment.setTestModel(mMockMainViewModel);
        startFragment(mMainFragment, MainActivity.class);
    }

    /*@Test
    public void test_Activity() throws Exception {
        // See: http://robolectric.org/activity-lifecycle
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        Robolectric.setupService()
    }*/

    @Test
    public void init_shouldPopulateListWithDevices() throws Exception {
        RecyclerView rvList = (RecyclerView) mMainFragment.getView().findViewById(R.id.rvList);
        assertTrue(rvList.getAdapter().getItemCount() == mDevices.size());
    }

    @Test
    public void clickedRefresh_shouldPopulateListWithDevices() throws Exception {
        // When
        MenuItem menuItem = new RoboMenuItem(R.id.action_refresh);
        mMainFragment.onOptionsItemSelected(menuItem);

        // Then
        RecyclerView rvList = (RecyclerView) mMainFragment.getView().findViewById(R.id.rvList);
        assertTrue(rvList.getAdapter().getItemCount() == mDevices.size());
    }

    @Test
    public void clickedDeviceSwitch_shouldActivateDevice() throws Exception {
        // Given
        int index = 0;
        boolean switchState = mDevices.get(index).isOn();

        // When
        RecyclerView rvList = (RecyclerView) mMainFragment.getView().findViewById(R.id.rvList);
        rvList.measure(0, 0);
        rvList.layout(0, 0, 100, 10000);
        DevicesAdapter.ViewHolder viewHolder = (DevicesAdapter.ViewHolder) rvList.findViewHolderForLayoutPosition(0);
        viewHolder.swState.setPressed(true);
        viewHolder.swState.performClick();

        // Then
        verify(mMockMainViewModel).activateDevice(mDevices.get(index), !switchState);
    }
}
