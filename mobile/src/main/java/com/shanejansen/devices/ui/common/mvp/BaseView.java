package com.shanejansen.devices.ui.common.mvp;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Shane Jansen on 5/26/16.
 *
 * Base View for the MVP architecture.
 */
public interface BaseView {
    Context getAppContext();
    Activity getActivityContext();
}
