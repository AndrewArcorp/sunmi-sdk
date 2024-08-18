package com.example.sunmi_p2_sdk;

import androidx.annotation.NonNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import sunmi.paylib.SunmiPayKernel;

public class MainActivity extends FlutterActivity implements OnConnectCallback, ICardDelegate, IPrinterDelegate {

    private static final String CHANNEL = "sunmi_p2_sdk";
    private static final String EVENT_CHANNEL = "sunmi_p2_sdk_event";
    private static final String EVENT_CHANNEL_CARD_CHECKING = "checking_card";
    private static final String EVENT_CHANNEL_PRINTER_STATUS = "printer_status";


    private EventChannel.EventSink eventSink = null;
    private EventChannel.EventSink cardCheckingEventSink = null;
    private EventChannel.EventSink printerStatusEventSink = null;

    private MethodChannel.Result result = null;

    private SunmiSDK sunmiSDK;
    private CardReader cardReader;
    private Printer printer;


    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        initializeSDK();
        setupMethodChannel(flutterEngine);
        setupEventChannel(flutterEngine);
        setupEventChannelCardChecking(flutterEngine);
        setupEventChannelPrinterStatus(flutterEngine);
    }

    private void initializeSDK() {
        sunmiSDK = new SunmiSDK(this);
        cardReader = new CardReader(this);
        printer = new Printer(this, this);
    }

    private void setupMethodChannel(FlutterEngine flutterEngine) {
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler((call, result) -> {
            this.result = result;

            switch (call.method) {
                case "init":
                    sunmiSDK.initPaySDKService(this);
                    break;
                case "destroy":
                    new Thread(() -> sunmiSDK.destroyPaySDKService()).start();
                    break;
                case "checkCard":
                    new Thread(() -> cardReader.checkCard()).start();
                    break;
                case "cancelCheck":
                    new Thread(() -> cardReader.cancelCheckCard()).start();
                    break;
                case "print":
                    new Thread(() -> printer.print(call.argument("text"))).start();
                    break;
                case "printBitmap":
                    new Thread(() -> printer.printBitmap(call.argument("bitmap"))).start();
                    break;
                default:
                    result.notImplemented();
                    break;
            }
        });
    }

    private void setupEventChannel(FlutterEngine flutterEngine) {
        new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), EVENT_CHANNEL).setStreamHandler(
                new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object arguments, EventChannel.EventSink events) {
                        eventSink = events;
                    }

                    @Override
                    public void onCancel(Object arguments) {
                        eventSink = null;
                    }
                }
        );
    }

    private void setupEventChannelCardChecking(FlutterEngine flutterEngine) {
        new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), EVENT_CHANNEL_CARD_CHECKING).setStreamHandler(
                new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object arguments, EventChannel.EventSink events) {
                        cardCheckingEventSink = events;
                    }

                    @Override
                    public void onCancel(Object arguments) {
                        cardCheckingEventSink = null;
                    }
                }
        );
    }

    private void setupEventChannelPrinterStatus(FlutterEngine flutterEngine) {
        new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), EVENT_CHANNEL_PRINTER_STATUS).setStreamHandler(
                new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object arguments, EventChannel.EventSink events) {
                        printerStatusEventSink = events;
                    }

                    @Override
                    public void onCancel(Object arguments) {
                        printerStatusEventSink = null;
                    }
                }
        );
    }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            sunmiSDK.destroyPaySDKService();
            sunmiSDK = null;
            eventSink = null;
        }

        @Override
        public void onConnectPaySDK(SunmiPayKernel kernel) {
            runOnUiThread(() -> {
                sendEvent("KernelConnected");
                cardReader.setReadCardOptV2(kernel.mReadCardOptV2);
                if (result != null) {
                    result.success(true);
                    result = null;
                }
            });
        }

        @Override
    public void onDisconnectPaySDK(SunmiPayKernel kernel) {
        runOnUiThread(() -> {
            sendEvent("KernelDisConnected");
            cardReader.setReadCardOptV2(null);
            if (result != null) {
                result.success(true);
                result = null;
            }
        });
    }

    private void sendEvent(String event) {
        if (eventSink == null) return;
        eventSink.success(event);
    }

    @Override
    public void onCardCheckingStatusChanged(boolean isChecking) {
        if (cardCheckingEventSink == null) return;
        runOnUiThread(() -> {
            cardCheckingEventSink.success(isChecking);
        });
    }

    @Override
    public void onCardFailed(CardData data) {
        if (result != null) {
            runOnUiThread(() -> {
                result.error(data.errorMsg, data.errorMsg, data.toMap());
                result = null;
            });
        }
    }

    @Override
    public void onCardSuccess(CardData data) {
        if (result != null) {
            runOnUiThread(() -> {
                result.success(data.toMap());
                result = null;
            });
        }
    }

    @Override
    public void onPrintConnected(boolean isConnected) {
        if (printerStatusEventSink == null) return;
        runOnUiThread(() -> {
            printerStatusEventSink.success(isConnected);
        });
    }
}
