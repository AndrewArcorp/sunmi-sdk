package com.example.sunmi_p2_sdk;

public interface ICardDelegate {
    void onCardCheckingStatusChanged(boolean isChecking);
    void onCardFailed(CardData message);
    void onCardSuccess(CardData message);

}
