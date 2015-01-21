package com.example.webgallery.activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.webgallery.R;
import com.example.webgallery.activity.view.RecyclerViewAdapter;
import com.example.webgallery.threads.DataHandler;
import com.example.webgallery.threads.GetJsonTask;

import java.net.MalformedURLException;
import java.net.URL;

public class WebGalleryActivity extends Activity {
    private final String GET_REQUEST = "http://162.243.129.214/api/test";
    private String mRequest;
    private EditText mAddressEditText;
    private Button mStartButton;
    private Button mStopButton;
    private RecyclerView mPicturesListView;
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
        mDownloadsHandler = new DataHandler();
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
        RecyclerView.LayoutManager manager = new LinearLayoutManager(WebGalleryActivity.this);

        mPicturesListView = (RecyclerView) findViewById(R.id.recycler_view);
        mPicturesListView.setLayoutManager(manager);


    }

    public void startDownload(View startButton) {
        switchButtons();

        if (!isInternetEnabled()) {
            Toast.makeText(WebGalleryActivity.this, "Internet connection unavailable.\nCheck network settings",
                    Toast.LENGTH_LONG).show();
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


        Thread downloadThread = new Thread(new GetJsonTask(url, mDownloadsHandler));
        downloadThread.start();

        RecyclerView.Adapter adapter = new RecyclerViewAdapter("STUB");
        mPicturesListView.setAdapter(adapter);
    }

    public void stopDownload(View stopButton) {
        switchButtons();
    }

    private void switchButtons() {
        mStartButton.setEnabled(!mStartButton.isEnabled());
        mStopButton.setEnabled(!mStopButton.isEnabled());
    }
}
