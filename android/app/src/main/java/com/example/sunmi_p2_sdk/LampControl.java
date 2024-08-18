package com.example.sunmi_p2_sdk;

import com.example.sunmi_p2_sdk.utils.LedColors;
import com.sunmi.pay.hardware.aidlv2.AidlConstantsV2;
import com.sunmi.pay.hardware.aidlv2.system.BasicOptV2;

public class LampControl {

    private BasicOptV2 basicOptV2;
    public void onClick(LedColors color ,boolean isOpen) {
        try {
            basicOptV2.ledStatusOnDevice(color.getValue(), isOpen?0 : 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBasicOptV2(BasicOptV2 basicOptV2) {
        this.basicOptV2 = basicOptV2;
    }
}
