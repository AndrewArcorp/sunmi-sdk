import 'dart:convert';

class CardData {
  final bool? isSuccess;
  final String? errorMsg;
  final String? track1;
  final String? track2;
  final String? track3;
  final String? atr;
  final String? ats;
  final String? uuid;
  final String? pan;
  final String? serviceCode;
  final String? type;

  CardData({
    required this.isSuccess,
    required this.errorMsg,
    required this.track1,
    required this.track2,
    required this.track3,
    required this.atr,
    required this.ats,
    required this.uuid,
    required this.pan,
    required this.serviceCode,
    required this.type,
  });

  factory CardData.fromMap(Map<dynamic, dynamic> map) {
    return CardData(
      isSuccess: map['isSuccess'],
      errorMsg: map['errorMsg'],
      track1: map['track1'],
      track2: map['track2'],
      track3: map['track3'],
      atr: map['atr'],
      ats: map['ats'],
      uuid: map['uuid'],
      pan: map['pan'],
      serviceCode: map['serviceCode'],
      type: map['type'],
    );
  }

  String toJson() {
    return jsonEncode({
      'isSuccess': isSuccess,
      'errorMsg': errorMsg,
      'track1': track1,
      'track2': track2,
      'track3': track3,
      'atr': atr,
      'ats': ats,
      'uuid': uuid,
      'pan': pan,
      'serviceCode': serviceCode,
      'type': type,
    });
  }
}
