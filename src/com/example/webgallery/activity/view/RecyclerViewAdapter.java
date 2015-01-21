package com.example.webgallery.activity.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.webgallery.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Moreno on 21.01.2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private JSONArray mData;

    public RecyclerViewAdapter(String data) {
        try {
            mData = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.gallery_item, viewGroup);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        try {
            JSONObject itemData= mData.getJSONObject(i);
            viewHolder.mAuthorTextView.setText(itemData.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthorTextView;
        public TextView mDateTextView;
        public ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.item_author_textview);
            mDateTextView = (TextView) itemView.findViewById(R.id.item_date_textview);
            mImageView = (ImageView) itemView.findViewById(R.id.item_imageview);
        }
    }
}
