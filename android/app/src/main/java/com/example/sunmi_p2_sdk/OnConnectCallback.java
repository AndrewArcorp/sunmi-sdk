package com.example.sunmi_p2_sdk;

import sunmi.paylib.SunmiPayKernel;

public interface OnConnectCallback {
    void onConnectPaySDK(SunmiPayKernel kernel);
    void onDisconnectPaySDK(SunmiPayKernel kernel);
}
