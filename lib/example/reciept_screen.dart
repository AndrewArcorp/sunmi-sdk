import 'package:flutter/material.dart';

class RecieptScreen extends StatelessWidget {
  final GlobalKey printKey;
  final ScrollController scrollController;

  const RecieptScreen(
      {super.key, required this.printKey, required this.scrollController});

  TextStyle get textStyle => const TextStyle(
        fontSize: 8,
        color: Colors.black,
      );

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 150,
      height: 500,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(
          color: Colors.black,
          width: 1,
        ),
      ),
      padding: const EdgeInsets.all(20),
      child: RepaintBoundary(
        key: printKey,
        child: Container(
          color: Colors.white,
          child: ListView(
            controller: scrollController,
            children: [
              Center(
                child: Image.asset(
                  "assets/Nike-Logo.png",
                  width: 50,
                  height: 50,
                ),
              ),
              const Divider(
                thickness: 1,
                color: Colors.black,
              ),
              const SizedBox(
                height: 20,
              ),
              Text(
                "Total: 1000",
                style: textStyle,
              ),
              const SizedBox(
                height: 20,
              ),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
              Text(
                "Payment Method: Cash",
                style: textStyle,
              ),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }
}
