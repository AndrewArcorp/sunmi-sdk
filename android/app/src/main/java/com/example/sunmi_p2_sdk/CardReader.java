package com.example.sunmi_p2_sdk;

import android.os.Bundle;
import android.os.RemoteException;

import com.example.sunmi_p2_sdk.utils.DeviceUtil;
import com.example.sunmi_p2_sdk.utils.LogUtil;
import com.example.sunmi_p2_sdk.utils.Utility;
import com.example.sunmi_p2_sdk.utils.ByteUtil;
import com.sunmi.pay.hardware.aidl.AidlConstants;
import com.sunmi.pay.hardware.aidlv2.readcard.CheckCardCallbackV2;
import com.sunmi.pay.hardware.aidlv2.readcard.ReadCardOptV2;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class CardReader {
    private ReadCardOptV2 readCardOptV2;
    private final ICardDelegate delegate;

    private boolean checkingCard;

    public CardReader(ICardDelegate delegate) {
        this.delegate = delegate;
        this.checkingCard = false;
    }

    public void setReadCardOptV2(ReadCardOptV2 readCardOptV2) {
        this.readCardOptV2 = readCardOptV2;
        delegate.onCardCheckingStatusChanged(this.checkingCard);
    }

    public void checkCard() {
        try {
            changeCardStatus(true);
            if (readCardOptV2 == null) {
                return;
            }
            int cardType = AidlConstants.CardType.MAGNETIC.getValue() | AidlConstants.CardType.IC.getValue() | AidlConstants.CardType.NFC.getValue();

            if (DeviceUtil.isTossTerminal()) {
                cardType &= ~AidlConstants.CardType.NFC.getValue();
            }
            if (DeviceUtil.isTossTerminal() || DeviceUtil.isTossFront()) {
                Bundle bundle = new Bundle();
                bundle.putInt("cardType", cardType);
                bundle.putInt("ctrCode", 0);
                bundle.putInt("code", 1);
                bundle.putInt("type", 2);
                bundle.putInt("maskStart", 3);
                bundle.putInt("maskEnd", 4);
                bundle.putChar("maskChar", '*');
                bundle.putInt("stopOnError", 0);
                readCardOptV2.checkCardForToss(bundle, mCheckCardCallback, 60);
            } else {
                readCardOptV2.checkCard(cardType, mCheckCardCallback, 60);
            }
        } catch (Exception e) {
            LogUtil.e("checkCard", e.getMessage());
            delegate.onCardFailed(CardData.fromError(e.getMessage()));
            changeCardStatus(false);
        }
    }

    private final CheckCardCallbackV2 mCheckCardCallback = new CheckCardCallbackV2Wrapper() {

        @Override
        public void findMagCard(Bundle info) throws RemoteException {
            LogUtil.i("findMagCard", "findMagCard:" + Utility.bundle2String(info));
            handleResult(0, true, info);
        }

        /**
         * Find IC card
         *
         * @param info return data，contain the following keys:
         *             <br/>cardType: card type (int)
         *             <br/>atr: card's ATR (String)
         */
        @Override
        public void findICCardEx(Bundle info) throws RemoteException {
            LogUtil.i("findICCard", "findICCard:" + Utility.bundle2String(info));
            handleResult(1, true, info);
        }

        /**
         * Find RF card
         *
         * @param info return data，contain the following keys:
         *             <br/>cardType: card type (int)
         *             <br/>uuid: card's UUID (String)
         *             <br/>ats: card's ATS (String)
         *             <br/>sak: card's SAK, if exist (int) (M1 S50:0x08, M1 S70:0x18, CPU:0x28)
         *             <br/>cardCategory: card's category,'A' or 'B', if exist (int)
         *             <br/>atqa: card's ATQA, if exist (byte[])
         */
        @Override
        public void findRFCardEx(Bundle info) throws RemoteException {
            LogUtil.i("findRFCard", "findRFCard:" + Utility.bundle2String(info));
            handleResult(2, true, info);
        }

        /**
         * Check card error
         *
         * @param info return data，contain the following keys:
         *             <br/>cardType: card type (int)
         *             <br/>code: the error code (String)
         *             <br/>message: the error message (String)
         */
        @Override
        public void onErrorEx(Bundle info) throws RemoteException {
            int code = info.getInt("code");
            String msg = info.getString("message");
            String error = "onError:" + msg + " -- " + code;
            LogUtil.e("onError", error);
            handleResult(-1, false, info);
        }
    };

    private void handleResult(int type, boolean success, Bundle info) {
        if (type == 0) {//磁卡
            if (DeviceUtil.isTossTerminal() || DeviceUtil.isTossFront()) {
                handleTossMagCard(success, info);
            } else {
                handleNormalMagCard(success, info);
            }
        } else if (type == 1) {
            handleICCard(success, info);
        } else if (type == 2) {
            handleNfcCard(success, info);
        }
        changeCardStatus(false);
    }

    private void handleTossMagCard(boolean success, Bundle info) {
        if (success) {
            //US-ASCII encoding string, encrypted data
            String track2 = Utility.null2String(info.getString("TRACK2"));
            //convert track2 to hex string
            track2 = ByteUtil.bytes2HexStr(track2.getBytes(StandardCharsets.US_ASCII));
            String pan = Utility.null2String(info.getString("pan"));
            String serviceCode = Utility.null2String(info.getString("servicecode"));
            //磁道错误码：0-无错误，-1-磁道无数据，-2-奇偶校验错，-3-LRC校验错
            int code2 = info.getInt("track2ErrorCode");
            LogUtil.e("handleTossMagCard", String.format(Locale.getDefault(),
                    "track2ErrorCode:%d,track2:%s\npan:%s\nserviceCode:%s",
                    code2, track2, pan, serviceCode));
            if (code2 == 0) {
                delegate.onCardSuccess(CardData.fromTossMagCard(track2, serviceCode, pan));
            } else {
                delegate.onCardFailed(CardData.fromError("find MagStripe card failed"));
            }
        } else {
            delegate.onCardFailed(CardData.fromError("find MagStripe card failed"));
        }
    }

    private void handleNormalMagCard(boolean success, Bundle info) {
        if (success) {
            String track1 = Utility.null2String(info.getString("TRACK1"));
            String track2 = Utility.null2String(info.getString("TRACK2"));
            String track3 = Utility.null2String(info.getString("TRACK3"));
            //磁道错误码：0-无错误，-1-磁道无数据，-2-奇偶校验错，-3-LRC校验错
            int code1 = info.getInt("track1ErrorCode");
            int code2 = info.getInt("track2ErrorCode");
            int code3 = info.getInt("track3ErrorCode");
            LogUtil.e("handleNormalMagCard", String.format(Locale.getDefault(),
                    "track1ErrorCode:%d,track1:%s\ntrack2ErrorCode:%d,track2:%s\ntrack3ErrorCode:%d,track3:%s",
                    code1, track1, code2, track2, code3, track3));
            if ((code1 != 0 && code1 != -1) || (code2 != 0 && code2 != -1) || (code3 != 0 && code3 != -1)) {
                delegate.onCardFailed(CardData.fromError("find Normal Mag card failed"));
            } else {
                delegate.onCardSuccess(CardData.fromNormalMagCard(track1, track2, track3));
            }
        } else {
            delegate.onCardFailed(CardData.fromError("find Normal Mag card failed"));
        }
    }

    private void handleICCard(boolean success, Bundle info) {
        if (success) {
            delegate.onCardSuccess(CardData.fromICCard(info.getString("atr")));
        } else {
            delegate.onCardFailed(CardData.fromError("find IC card failed"));
        }
    }

    private void handleNfcCard(boolean success, Bundle info) {
        if (success) {
            delegate.onCardSuccess(CardData.fromNFCCard(info.getString("uuid"), info.getString("ats")));
        } else {
            delegate.onCardFailed(CardData.fromError("find NFC card failed"));
        }
    }

    public void cancelCheckCard() {
        try {
            readCardOptV2.cardOff(AidlConstants.CardType.IC.getValue());
            readCardOptV2.cardOff(AidlConstants.CardType.NFC.getValue());
            readCardOptV2.cancelCheckCard();
            changeCardStatus(false);
        } catch (Exception e) {
            LogUtil.e("cancelCheckCard", e.getMessage());
            delegate.onCardFailed(CardData.fromError("Failed to cancel card"));
        }
    }

    private void changeCardStatus(boolean isChecking) {
        this.checkingCard = isChecking;
        delegate.onCardCheckingStatusChanged(isChecking);
    }
}
