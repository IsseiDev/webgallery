package com.example.webgallery;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v17.leanback.widget.VerticalGridView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class WebGalleryActivity extends Activity {
    private final String GET_REQUEST = "http://162.243.129.214/api/test";
    private String mRequest;
    private EditText mAddressEditText;
    private Button mStartButton;
    private Button mStopButton;
    private ListView mPicturesListView;
    private Handler mDownloadsHandler;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        initViews();
    }

    @Override
    protected void onResume() {
        mStartButton.setEnabled(true);
        mStopButton.setEnabled(false);
        super.onResume();
        mDownloadsHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.e("main", "message handled: " + msg.toString());
            }
        };
    }

    private boolean isInternetEnabled() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initViews() {
        mAddressEditText = (EditText) findViewById(R.id.link_edittext);
        mStartButton = (Button) findViewById(R.id.start_download_button);
        mStopButton = (Button) findViewById(R.id.stop_download_button);
        mPicturesListView = (ListView) findViewById(R.id.pictures_listview);

    }

    public void startDownload(View startButton) {
        switchButtons();

        if (!isInternetEnabled()) {
            Toast.makeText(WebGalleryActivity.this, "Internet connection unavailable.\nCheck network settings", Toast.LENGTH_LONG).show();
            return;
        }

        mRequest = mAddressEditText.getText().toString();

        if (mRequest.isEmpty()) {
            mRequest = GET_REQUEST;
        }
        final URL url;
        try {
            Log.e("main", "Request: " + mRequest);
            url = new URL(mRequest);
        } catch (MalformedURLException e) {
            mRequest = null;
            Toast.makeText(WebGalleryActivity.this, "Invalid address", Toast.LENGTH_SHORT).show();
            switchButtons();
            return;
        }


        Thread downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String data = "";
                try {

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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


                    ArrayList<Integer> bytes = new ArrayList<Integer>();
                    while (responseContent.available() == 1) {
                        bytes.add(responseContent.read());
                    }

                    data = new String();
                } catch (IOException e) {
                    Log.e("main", "IOException occured");
                    Log.e("main", e.toString());
                }

                try {
                    Log.e("main", "Data: " + data);
                    data = data.substring(0, data.lastIndexOf(']') + 1);
                    JSONArray jsonArray = new JSONArray(data);
                    mDownloadsHandler.sendEmptyMessage(5);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Log.e("main", "JSON object: " + obj.toString());
                    }

                } catch (JSONException e) {
                    Log.e("main", "JSONException occured");
                    Log.e("main", e.toString());
                }
            }
        });

        downloadThread.start();
    }

    public void stopDownload(View stopButton) {
        switchButtons();
    }

    private void switchButtons() {
        mStartButton.setEnabled(!mStartButton.isEnabled());
        mStopButton.setEnabled(!mStopButton.isEnabled());
    }
}
