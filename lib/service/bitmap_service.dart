import 'dart:ui' as ui;
import 'package:flutter/rendering.dart';
import 'package:flutter/material.dart';

class BitmapService {
  static Future<ui.Image> createImageFromWidget(GlobalKey key) async {
    // Get the render object of the widget
    RenderRepaintBoundary boundary =
        key.currentContext!.findRenderObject() as RenderRepaintBoundary;

    // Create an image from the boundary
    ui.Image image = await boundary.toImage(pixelRatio: 3.0);

    return image;
  }
}
