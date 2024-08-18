package com.example.sunmi_p2_sdk;

import java.util.HashMap;
import java.util.Map;

public class CardData {
    boolean isSuccess;
    String errorMsg;

    String track1;
    String track2;
    String track3;

    String atr;

    String ats;
    String uuid;

    String pan;
    String serviceCode;

    String type;

    public static CardData fromError(String errorMsg) {
        CardData cardData = new CardData();
        cardData.isSuccess = false;
        cardData.errorMsg = errorMsg;
        return cardData;
    }

    public static CardData fromTossMagCard(String track2, String serviceCode, String pan) {
        CardData cardData = new CardData();
        cardData.isSuccess = true;
        cardData.track2 = track2;
        cardData.serviceCode = serviceCode;
        cardData.pan = pan;
        cardData.type = "Toss";
        return cardData;
    }

    public static CardData fromNormalMagCard(String track1, String track2, String track3) {
        CardData cardData = new CardData();
        cardData.isSuccess = true;
        cardData.track1 = track1;
        cardData.track2 = track2;
        cardData.track3 = track3;
        cardData.type = "Normal";
        return cardData;
    }

    public static CardData fromICCard(String atr) {
        CardData cardData = new CardData();
        cardData.isSuccess = true;
        cardData.atr = atr;
        cardData.type = "IC";
        return cardData;
    }

    public static CardData fromNFCCard(String uuid, String ats) {
        CardData cardData = new CardData();
        cardData.isSuccess = true;
        cardData.uuid = uuid;
        cardData.ats = ats;
        cardData.type = "NFC";
        return cardData;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("isSuccess", isSuccess);
        map.put("errorMsg", errorMsg);
        map.put("track1", track1);
        map.put("track2", track2);
        map.put("track3", track3);
        map.put("atr", atr);
        map.put("ats", ats);
        map.put("uuid", uuid);
        map.put("pan", pan);
        map.put("serviceCode", serviceCode);
        map.put("type", type);
        return map;
    }
}
