package com.example.sunmi_p2_sdk;

import android.content.Context;

import com.example.sunmi_p2_sdk.utils.LogUtil;

import sunmi.paylib.SunmiPayKernel;

public class SunmiSDK {

    private final OnConnectCallback onConnectCallback;

    public SunmiSDK(OnConnectCallback onConnectCallback) {
        this.onConnectCallback = onConnectCallback;
    }

    public void initPaySDKService(Context context) {
        final SunmiPayKernel payKernel = SunmiPayKernel.getInstance();
        payKernel.initPaySDK(context, createConnectCallback(payKernel));
        LogUtil.e("initPaySDKService", "initPaySDKService");
    }

    public void destroyPaySDKService() {
        final SunmiPayKernel payKernel = SunmiPayKernel.getInstance();
        payKernel.destroyPaySDK();
        onConnectCallback.onDisconnectPaySDK(payKernel);
        LogUtil.e("destroyPaySDKService", "destroyPaySDKService");
    }

    private SunmiPayKernel.ConnectCallback createConnectCallback(SunmiPayKernel payKernel) {
        return new SunmiPayKernel.ConnectCallback() {
            @Override
            public void onConnectPaySDK() {
                LogUtil.e("onConnectPaySDK", "onConnectPaySDK");
                onConnectCallback.onConnectPaySDK(payKernel);
            }

            @Override
            public void onDisconnectPaySDK() {
                LogUtil.e("onDisconnectPaySDK", "onDisconnectPaySDK");
                onConnectCallback.onDisconnectPaySDK(payKernel);
            }
        };
    }
}
