package edu.dickinson.newmixxer;

import android.content.ComponentName;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsServiceConnection;

public class TabService extends CustomTabsServiceConnection {
    @Override
    public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
