package com.wlr.cerebralnfc;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

/**
 * @author:William Loveridge-Rushforth
 * @Summary: Emulates an NFC card.
 */
public class CerebralHostApduService extends HostApduService {


    // Reads APDU command sent by NFC reader.
    @Override
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {
        return new byte[0];
    }

    // Called when the NFC link is broken or has recieved a new APDU commands from the reader.
    @Override
    public void onDeactivated(int i) {

    }
}
