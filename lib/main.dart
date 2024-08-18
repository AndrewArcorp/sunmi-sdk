import 'package:flutter/material.dart';
import 'package:sunmi_p2_sdk/example/home_screen.dart';
import 'package:sunmi_p2_sdk/service/service_locator.dart';
import 'package:sunmi_p2_sdk/service/sunmi_sdk.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  setup();
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      getIt<SunmiSdk>().bindPaySDK();
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SDk',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: StreamBuilder<bool>(
        stream: getIt<SunmiSdk>().sdkBindingStatus(),
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            if (snapshot.data == true) {
              return const HomeScreen();
            } else {
              return const Scaffold(
                body: Center(
                  child: CircularProgressIndicator(),
                ),
              );
            }
          } else {
            return const Scaffold(
              body: Center(
                child: CircularProgressIndicator(),
              ),
            );
          }
        },
      ),
    );
  }

  @override
  void dispose() {
    getIt<SunmiSdk>().destroyPaySDK();
    super.dispose();
  }
}
