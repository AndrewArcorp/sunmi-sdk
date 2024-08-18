package com.example.sunmi_p2_sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;

import com.example.sunmi_p2_sdk.utils.LogUtil;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

public class Printer {
    private SunmiPrinterService sunmiPrinterService;
    private IPrinterDelegate delegate;

    public Printer(Context context, IPrinterDelegate delegate) {
        this.delegate = delegate;
        try {
            InnerPrinterManager.getInstance().bindService(context, new InnerPrinterCallback() {
                @Override
                protected void onConnected(SunmiPrinterService service) {
                    sunmiPrinterService = service;
                    delegate.onPrintConnected(true);
                }

                @Override
                protected void onDisconnected() {
                    sunmiPrinterService = null;
                    delegate.onPrintConnected(false);
                }
            });
        } catch (Exception e) {
            LogUtil.e("Printer", e.getMessage());
            delegate.onPrintConnected(false);
        }
    }

    public void print(String text) {
        try {
            sunmiPrinterService.enterPrinterBuffer(true);
            sunmiPrinterService.printTextWithFont(text + "\n", "", 20, innerResultCallbcak);
            sunmiPrinterService.lineWrap(6, innerResultCallbcak);
            byte[] returnText = new byte[3];
            returnText[0] = 0x1B;
            returnText[1] = 0x33;
            returnText[2] = (byte) 30;
            sunmiPrinterService.sendRAWData(returnText, null);
            sunmiPrinterService.exitPrinterBufferWithCallback(true, innerResultCallbcak);
        } catch (Exception e) {
            LogUtil.e("print", e.getMessage());
        }
    }

    public void printBitmap(byte[] byteArray) {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            sunmiPrinterService.enterPrinterBuffer(true);
            sunmiPrinterService.printBitmap(bitmap, innerResultCallbcak);
            sunmiPrinterService.lineWrap(4, null);
            sunmiPrinterService.exitPrinterBuffer(true);
        } catch (Exception e) {
            LogUtil.e("printBitmap", e.getMessage());
        }
    }

    private final InnerResultCallback innerResultCallbcak = new InnerResultCallback() {
        @Override
        public void onRunResult(boolean isSuccess) {
            LogUtil.e("lxy", "isSuccess:" + isSuccess);
            if (isSuccess) {

            }
        }

        @Override
        public void onReturnString(String result) {
            LogUtil.e("lxy", "result:" + result);
        }

        @Override
        public void onRaiseException(int code, String msg) {
            LogUtil.e("lxy", "code:" + code + ",msg:" + msg);
        }

        @Override
        public void onPrintResult(int code, String msg) {
            LogUtil.e("lxy", "code:" + code + ",msg:" + msg);
        }
    };
}
