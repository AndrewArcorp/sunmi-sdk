class FailedToBindPaySDKException implements Exception {
  final String message;
  final StackTrace? stackTrace;
  final Object? error;

  FailedToBindPaySDKException(this.message, {this.stackTrace, this.error});

  @override
  String toString() {
    return 'FailedToBindPaySDKException: $message';
  }
}

class FailedToDestroyPaySDKException implements Exception {
  final String message;
  final StackTrace? stackTrace;
  final Object? error;

  FailedToDestroyPaySDKException(this.message, {this.stackTrace, this.error});

  @override
  String toString() {
    return 'FailedToDestroyPaySDKException: $message';
  }
}

class FailedToScanCardException implements Exception {
  final String message;
  final StackTrace? stackTrace;
  final Object? error;

  FailedToScanCardException(this.message, {this.stackTrace, this.error});

  @override
  String toString() {
    return 'FailedToScanCardException: $message';
  }
}
