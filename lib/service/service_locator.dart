import 'package:get_it/get_it.dart';
import 'package:logger/logger.dart';
import 'package:sunmi_p2_sdk/service/card_reader.dart';
import 'package:sunmi_p2_sdk/service/led_control.dart';
import 'package:sunmi_p2_sdk/service/printer_service.dart';
import 'package:sunmi_p2_sdk/service/sunmi_sdk.dart';

final getIt = GetIt.instance;

void setup() {
  getIt.registerLazySingleton<SunmiSdk>(() => SunmiSdk());
  getIt.registerLazySingleton<CardReader>(() => CardReader());
  getIt.registerLazySingleton<Logger>(() => Logger());
  getIt.registerLazySingleton<PrinterService>(() => PrinterService());
  getIt.registerLazySingleton<LedControl>(() => LedControl());
}
