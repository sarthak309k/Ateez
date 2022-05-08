package com.first.ateez.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

public class Otpreceive extends BroadcastReceiver {
    public static EditText finalsetotp;
    public void setSetotp(EditText setotp)
    {
        Otpreceive.finalsetotp=setotp;
    }
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onReceive(Context context, Intent intent) {

            SmsMessage[] messagess= Telephony.Sms.Intents.getMessagesFromIntent(intent);
            for(SmsMessage sms:messagess)
            {
                String msg=sms.getMessageBody();
                String getOTP = msg.split(":")[1];
                finalsetotp.setText(getOTP);
            }
        }

    }

