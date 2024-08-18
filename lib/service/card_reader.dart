import 'package:flutter/services.dart';
import 'package:logger/logger.dart';
import 'package:sunmi_p2_sdk/model/card_data.dart';
import 'package:sunmi_p2_sdk/utils/constants.dart';
import 'package:sunmi_p2_sdk/utils/exceptions.dart';

class CardReader {
  late MethodChannel platform;
  late Logger logger;
  late EventChannel checkingCardEventChannel;

  CardData? data;

  CardReader() {
    platform = const MethodChannel(Constants.sunmiP2SdkChannel);
    logger = Logger();
    checkingCardEventChannel =
        const EventChannel(Constants.checkingCardEventChannel);
  }

  Future<CardData?> scanCard() async {
    try {
      final result = await platform.invokeMethod(Constants.checkCardMethod);
      logger.i('Scanned card: $result');
      data = CardData.fromMap(result);
      return data;
    } on PlatformException {
      logger.e('Failed to scan card', stackTrace: StackTrace.current);
    } catch (e) {
      logger.e('Failed to scan card', stackTrace: StackTrace.current, error: e);
    }
    throw FailedToScanCardException('Failed to scan card');
  }

  Future<bool> cancelCard() async {
    try {
      await platform.invokeMethod(Constants.cancelCheckMethod);
      return true;
    } on PlatformException {
      logger.e('Failed to cancel card', stackTrace: StackTrace.current);
    } catch (e) {
      logger.e('Failed to cancel card',
          stackTrace: StackTrace.current, error: e);
    }
    return false;
  }

  Stream<bool> streamCheckingCard() {
    return checkingCardEventChannel
        .receiveBroadcastStream()
        .map((event) => event as bool?)
        .map((event) => event ?? false);
  }
}
