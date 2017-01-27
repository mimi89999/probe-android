package org.openobservatory.ooniprobe.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import org.openobservatory.ooniprobe.activity.MainActivity;
import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.data.TestData;
import org.openobservatory.ooniprobe.data.TestStorage;
import org.openobservatory.ooniprobe.model.NetworkMeasurement;
import org.openobservatory.ooniprobe.model.OONITests;
import org.openobservatory.ooniprobe.utils.Alert;
import android.support.v7.widget.PopupMenu;

import org.openobservatory.ooniprobe.utils.LogUtils;
import org.openobservatory.ooniprobe.view.ListImageButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;

import static org.openobservatory.ooniprobe.R.id.progressBar;

public class RunTestListAdapter extends RecyclerView.Adapter<RunTestListAdapter.ViewHolder> {

    private static final String TAG = RunTestListAdapter.class.toString();

    private MainActivity mActivity;
    private LinkedHashMap<String,Boolean> values;
    private int context;
    OnItemClickListener mItemClickListener;
    private String[] keys;

    public RunTestListAdapter(MainActivity context, LinkedHashMap<String,Boolean> values) {
        this.mActivity = context;
        this.values = values;
        this.keys = values.keySet().toArray(new String[values.size()]);
    }

    @Override
    public RunTestListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_run_test, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RunTestListAdapter.ViewHolder holder, int position) {
        final String key = keys[position];
        Boolean available = getItem(position);
        holder.txtTitle.setText(NetworkMeasurement.getTestName(mActivity, key));
        holder.txtDesc.setText(NetworkMeasurement.getTestDescr(mActivity, key));
        holder.testImage.setImageResource(NetworkMeasurement.getTestImage(key, true));
        if (available) {
            holder.progressIndicator.setVisibility(View.GONE);
            holder.runTest.setVisibility(View.VISIBLE);
        }
        else {
            holder.runTest.setVisibility(View.GONE);
            holder.progressIndicator.setVisibility(View.VISIBLE);
        }
        holder.runTest.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        TestData.doNetworkMeasurements(mActivity, key);
                    }
                }
        );
        holder.itemView.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Alert.alertMdWebView(mActivity, key);
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public Boolean getItem(int position) {
        return values.get(keys[position]);
    }

    public void setData(LinkedHashMap<String,Boolean> data) {
        values = data;
        keys = data.keySet().toArray(new String[values.size()]);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtTitle;
        public TextView txtDesc;
        public Button runTest;
        public ProgressBar progressBar;
        public ProgressBar progressIndicator;
        public ImageView testImage;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtTitle = (TextView) itemView.findViewById(R.id.test_title);
            txtDesc = (TextView) itemView.findViewById(R.id.test_desc);
            runTest = (Button) itemView.findViewById(R.id.run_test_button);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            progressIndicator = (ProgressBar) itemView.findViewById(R.id.progressIndicator);
            testImage = (ImageView) itemView.findViewById(R.id.test_logo);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
