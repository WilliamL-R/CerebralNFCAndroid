package com.wlr.cerebralnfc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.nfc.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.nio.charset.Charset;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText tagText;
    Button tagButton;
    NdefRecord tagRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagText = (EditText)findViewById(R.id.EditTagText);
        tagButton = (Button)findViewById(R.id.TagButton);
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagRecord = createTextTag(tagText.toString(), Locale.UK, true);
                Log.d("NdefRecord", tagRecord.toString());
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

}
