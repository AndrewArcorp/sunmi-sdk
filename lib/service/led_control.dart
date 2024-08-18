import 'package:flutter/services.dart';
import 'package:logger/logger.dart';
import 'package:sunmi_p2_sdk/model/card_data.dart';
import 'package:sunmi_p2_sdk/utils/constants.dart';

class LedControl {
  late MethodChannel platform;
  late Logger logger;

  CardData? data;

  LedControl() {
    platform = const MethodChannel(Constants.sunmiP2SdkChannel);
    logger = Logger();
  }

  Future<void> openLed() async {
    try {
      await platform.invokeMethod(Constants.openLed, {'color': 'RED_LIGHT'});
    } catch (e) {
      logger.e('Failed to open Led', stackTrace: StackTrace.current, error: e);
    }
  }

  Future<void> closeLed() async {
    try {
      await platform.invokeMethod(Constants.closeLed, {'color': 'RED_LIGHT'});
    } catch (e) {
      logger.e('Failed to close Led', stackTrace: StackTrace.current, error: e);
    }
  }
}
