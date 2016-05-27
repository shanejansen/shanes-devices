package com.shanejansen.devices.data;

import com.shanejansen.devices.data.models.Device;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Shane Jansen on 5/27/16.
 */

@RunWith(RobolectricTestRunner.class)
public class DataManagerTest {
    @Test
    public void refreshDevices() throws Exception {
        DataManager.refreshDevices(new DataManager.NetworkInf<List<Device>>() {
            @Override
            public void onCompleted(List<Device> result) {
                assertTrue(result.size() > 0);
            }
        });
    }
}
