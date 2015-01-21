package com.example.webgallery.threads;


import android.os.Handler;
import android.os.Message;

/**
 * Created by Moreno on 21.01.2015.
 */
public class DataHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 1:
                String data = (String) msg.obj;
                break;
            default:
                break;
        }
    }
}
