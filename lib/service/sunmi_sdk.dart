import 'dart:async';

import 'package:flutter/services.dart';
import 'package:logger/logger.dart';
import 'package:sunmi_p2_sdk/utils/constants.dart';
import 'package:sunmi_p2_sdk/utils/exceptions.dart';

class SunmiSdk {
  late MethodChannel platform;
  late Logger logger;
  late EventChannel eventChannel;

  final StreamController<bool> _sdkBindingStatus =
      StreamController<bool>.broadcast();

  Stream<bool> get sdkBindingStatusStream => _sdkBindingStatus.stream;

  SunmiSdk() {
    platform = const MethodChannel(Constants.sunmiP2SdkChannel);
    logger = Logger();
    eventChannel = const EventChannel(Constants.sunmiP2SdkEventChannel);
    // bindPaySDK();
  }

  Future<bool> bindPaySDK() async {
    try {
      bool? result = await platform.invokeMethod<bool>(Constants.initMethod);
      if (result == true) {
        logger.i('PaySDK bound');
      } else {
        logger.e('Failed to bind PaySDK');
      }
      _sdkBindingStatus.add(result ?? false);
      return result ?? false;
    } on PlatformException {
      logger.e('Failed to bind PaySDK', stackTrace: StackTrace.current);
    } catch (e) {
      logger.e('Failed to bind PaySDK',
          stackTrace: StackTrace.current, error: e);
    }
    throw FailedToBindPaySDKException('Failed to bind PaySDK');
  }

  Future<bool> destroyPaySDK() async {
    try {
      final result = await platform.invokeMethod<bool>(Constants.destroyMethod);
      if (result == true) {
        logger.i('PaySDK destroyed');
        _sdkBindingStatus.add(false);
      } else {
        logger.e('Failed to destroy PaySDK');
      }
      return result ?? false;
    } on PlatformException {
      logger.e('Failed to destroy PaySDK', stackTrace: StackTrace.current);
    } catch (e) {
      logger.e('Failed to destroy PaySDK',
          stackTrace: StackTrace.current, error: e);
    }
    throw FailedToDestroyPaySDKException('Failed to destroy PaySDK');
  }

  Stream<bool> sdkBindingStatus() {
    return eventChannel
        .receiveBroadcastStream()
        .map((event) => event.toString())
        .map(
      (event) {
        return event == 'KernelConnected';
      },
    );
  }
}
