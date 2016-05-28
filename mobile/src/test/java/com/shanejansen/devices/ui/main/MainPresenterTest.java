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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        mDevices.add(new Device());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                mPresenter.onLoadedDevices(mDevices);
                return null;
            }
        }).when(mMockModel).loadDevices();
    }

    @Test
    public void initView_shouldNotifyDevicesChanged() throws Exception {
        // When
        mPresenter.initView();

        // Then
        verify(mMockView).notifyDevicesChanged(mDevices);
    }

    @Test
    public void clickedRefresh_shouldNotifyDevicesChanged() throws Exception {
        // When
        mPresenter.clickedRefresh();

        // Then
        verify(mMockView).notifyDevicesChanged(mDevices);
    }

    @Test
    public void toggleDeviceSwitch_shouldActivateDevice() throws Exception {
        // Given
        int index = 0;
        boolean isChecked = true;

        // When
        when(mMockModel.getDevices()).thenReturn(mDevices);
        mPresenter.toggledDeviceSwitch(index, isChecked);

        // Then
        verify(mMockModel).activateDevice(mDevices.get(index), isChecked);
        assertTrue(mDevices.get(index).isOn());
    }
}
