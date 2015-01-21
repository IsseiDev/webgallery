package com.example.webgallery.threads;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Moreno on 21.01.2015.
 */
public class GetJsonTask implements Runnable {

    public static final int BUFFER_SIZE = 1024;
    public static final int JSON_STRING = 1;
    private final URL mUrl;
    private final Handler mHandler;

    public GetJsonTask(URL url, Handler handler) {
        mUrl = url;
        mHandler = handler;
    }

    @Override
    public void run() {
        String data = "";
        try {

            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(20000);
            connection.setDoInput(true);
            connection.connect();
            InputStream responseContent = connection.getInputStream();
            Log.e("main", "The response is: " + connection.getResponseCode());
            Log.e("main", "The response is: " + connection.getResponseMessage());
            Log.e("main", "Response class is: " + responseContent.getClass().getSimpleName());
            Log.e("main", "Response is: " + responseContent.toString());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            while (responseContent.available() == 1) {
                int size = responseContent.read(buffer);
                out.write(buffer, 0, size);
            }

            data = out.toString();
            out.close();
        } catch (IOException e) {
            Log.e("main", "IOException occured");
            Log.e("main", e.toString());
        }

        Message message = new Message();
        message.what = JSON_STRING;
        message.obj = data;
        mHandler.sendMessage(message);

        try {
            Log.e("main", "Data: " + data);
//            data = data.substring(0, data.lastIndexOf(']') + 1);
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Log.e("main", "JSON object: " + obj.toString());
            }

        } catch (JSONException e) {
            Log.e("main", "JSONException occured");
            Log.e("main", e.toString());
        }
    }

}

