package com.shanejansen.devices.ui.main;

import com.shanejansen.devices.data.models.Device;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

/**
 * Created by Shane Jansen on 5/27/16.
 */

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {
    private MainPresenter mPresenter;
    private List<Device> mDevices;
    @Mock
    private MainViewModel mMockModel;
    @Mock
    private MvpMain.ViewForPresenterOps mMockView;

    @Before
    public void setup() {
        // Bind these mocks
        mPresenter = new MainPresenter();
        mPresenter.bindModel(mMockModel);
        mPresenter.bindView(mMockView);

        Device device = mock(Device.class);
        mDevices = new ArrayList<>();
        mDevices.add(device);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                mPresenter.onLoadedDevices(mDevices);
                return null;
            }
        }).when(mMockModel).loadDevices();
    }

    @Test
    public void shouldNotifyDevicesChangedWhenInitialized() throws Exception {
        // When
        mPresenter.initView();

        // Then
        verify(mMockView).notifyDevicesChanged(mDevices);
    }

    @Test
    public void shouldNotifyDevicesChangedWhenRefreshed() throws Exception {
        // When
        mPresenter.clickedRefresh();

        // Then
        verify(mMockView).notifyDevicesChanged(mDevices);
    }
}
