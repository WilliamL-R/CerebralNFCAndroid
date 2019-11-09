package com.wlr.cerebralnfc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.nfc.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText tagText;
    Button tagButton;
    NdefRecord tagRecord;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagText = findViewById(R.id.EditTagText);
        tagButton = findViewById(R.id.TagButton);
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagRecord = createTextTag(tagText.getText().toString(), Locale.UK, true);
                Log.d("NdefRecord", tagRecord.toString());
                message = getTextFromNdefRecord(tagRecord);
                Log.d("message", message);
            }
        });
    }

    public NdefRecord createTextTag(String tagString, Locale locale, boolean encodeUtf8){

        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = tagString.getBytes(utfEncoding);
        int utfBit = encodeUtf8 ? 0 : (1 << 7);

        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;

        System.arraycopy(langBytes, 0 , data, 1, langBytes.length);
        System.arraycopy(textBytes, 0 , data, 1 + langBytes.length, textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
                new byte[0], data);

        return record;
    }

    private String getTextFromNdefRecord (NdefRecord ndefRecord)
    {
        String content = null;

        try
        {
            byte[] payload = ndefRecord.getPayload();
            String encoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTf-8";

            int languageSize = payload[0] & 0063;

            content = new String(payload, languageSize+1, payload.length - languageSize - 1, encoding);
        }
        catch (UnsupportedEncodingException e)
        {
            Log.e("createTextRecord", e.getMessage());
        }

        return content;
    }

}
