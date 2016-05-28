package com.shanejansen.devices.ui.main;

import android.support.v4.app.FragmentActivity;

import com.shanejansen.devices.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

/**
 * Created by Shane Jansen on 5/28/16.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainFragmentTest {

    @Before
    public void setup() {

    }

    @Test
    public void test_Activity() throws Exception {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void test_Fragment() throws Exception {
        MainFragment mainFragment = new MainFragment();
        startFragment(mainFragment, MainActivity.class);
        assertNotNull(mainFragment);
    }
}
