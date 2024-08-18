import 'package:flutter/material.dart';
import 'package:sunmi_p2_sdk/example/reciept_screen.dart';
import 'package:sunmi_p2_sdk/service/card_reader.dart';
import 'package:sunmi_p2_sdk/service/led_control.dart';
import 'package:sunmi_p2_sdk/service/printer_service.dart';
import 'package:sunmi_p2_sdk/service/service_locator.dart';

import 'package:sunmi_p2_sdk/service/bitmap_service.dart';
import 'dart:ui' as ui;
import 'dart:typed_data';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  GlobalKey printKey = GlobalKey();
  ScrollController scrollController = ScrollController();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Sunmi P2 SDK Example'),
      ),
      body: SizedBox(
        width: double.infinity,
        child: StreamBuilder<bool>(
          stream: getIt<CardReader>().streamCheckingCard(),
          builder: (context, snapshot) {
            if (snapshot.data == true) {
              return Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Expanded(
                      child: Center(
                        child: CircularProgressIndicator(),
                      ),
                    ),
                    ElevatedButton(
                      style: ButtonStyle(
                        minimumSize:
                            WidgetStateProperty.all(const Size(200, 50)),
                        shape: WidgetStateProperty.all(
                          RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(10),
                          ),
                        ),
                      ),
                      onPressed: () {
                        getIt<CardReader>().cancelCard();
                      },
                      child: const Text("Cancel Cheking Card"),
                    ),
                    const SizedBox(
                      height: 20,
                    ),
                  ],
                ),
              );
            }
            return SingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisSize: MainAxisSize.max,
                children: [
                  SingleChildScrollView(
                    child: RecieptScreen(
                      printKey: printKey,
                      scrollController: scrollController,
                    ),
                  ),
                  const SizedBox(
                    height: 20,
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      ElevatedButton(
                        style: ButtonStyle(
                          shape: WidgetStateProperty.all(
                            RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                            ),
                          ),
                        ),
                        onPressed: () {
                          getIt<CardReader>().scanCard();
                        },
                        child: const Text("Scan Card"),
                      ),
                      ElevatedButton(
                        style: ButtonStyle(
                          shape: WidgetStateProperty.all(
                            RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                            ),
                          ),
                        ),
                        onPressed: () {
                          captureImage();
                        },
                        child: const Text("Print"),
                      ),
                    ],
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      ElevatedButton(
                        style: ButtonStyle(
                          shape: WidgetStateProperty.all(
                            RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                            ),
                          ),
                        ),
                        onPressed: () {
                          getIt<LedControl>().openLed();
                        },
                        child: const Text("open Led"),
                      ),
                      ElevatedButton(
                        style: ButtonStyle(
                          shape: WidgetStateProperty.all(
                            RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                            ),
                          ),
                        ),
                        onPressed: () {
                          getIt<LedControl>().closeLed();
                        },
                        child: const Text("close Led"),
                      ),
                    ],
                  )
                ],
              ),
            );
          },
        ),
      ),
    );
  }

  Future<Uint8List> captureImage() async {
    ui.Image image = await BitmapService.createImageFromWidget(printKey);

    // Do something with the image, e.g., convert to bytes or save it
    ByteData? byteData = await image.toByteData(format: ui.ImageByteFormat.png);
    Uint8List pngBytes = byteData!.buffer.asUint8List();
    getIt<PrinterService>().printBitmap(pngBytes);

    scrollController.animateTo(
      500,
      duration: const Duration(seconds: 2),
      curve: Curves.linear,
    );
    // showDialog(
    //   context: context,
    //   builder: (context) {
    //     return AlertDialog(
    //       title: Text('Image Captured'),
    //       content: Image.memory(pngBytes),
    //       actions: [
    //         TextButton(
    //           onPressed: () {
    //             Navigator.of(context).pop();
    //           },
    //           child: Text('Close'),
    //         ),
    //       ],
    //     );
    //   },
    // );

    return pngBytes;
  }
}
