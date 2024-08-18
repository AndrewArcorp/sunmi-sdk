import 'package:flutter/services.dart';
import 'package:sunmi_p2_sdk/utils/constants.dart';

class PrinterService {
  late MethodChannel platform;

  PrinterService() {
    platform = const MethodChannel(Constants.sunmiP2SdkChannel);
  }

  Future<void> printText(String text) async {
    await platform
        .invokeMethod(Constants.print, {'text': 'Card scanned: $text'});
  }

  Future<void> printBitmap(Uint8List data) async {
    await platform.invokeMethod(Constants.printBitmap, {'bitmap': data});
  }
}
