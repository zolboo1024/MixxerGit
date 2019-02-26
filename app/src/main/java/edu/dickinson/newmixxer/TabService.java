package edu.dickinson.newmixxer;

import android.content.ComponentName;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;

public class TabService extends CustomTabsServiceConnection {
    @Override
    public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
