package com.beaconify.detect.beaconify;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.beaconify.detect.beaconify.Model.Beacon;

import java.util.LinkedHashMap;
import java.util.Set;


public class BeaconViewAdapter extends RecyclerView.Adapter<BeaconViewAdapter.ViewHolder> {

    private LinkedHashMap<String, Beacon> mDataset;

    public BeaconViewAdapter(LinkedHashMap<String, Beacon> dataset) {
        mDataset = dataset;
    }

    @Override
    public BeaconViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new BeaconView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(BeaconViewAdapter.ViewHolder holder, int position) {
        holder.getCell().bind(getItem(position));
    }

    private Beacon getItem(int position) {
        return mDataset.get(getItemKey(position));
    }

    private String getItemKey(int position) {
        Set<String> keys = mDataset.keySet();
        int i = 0;
        for (String key : keys) {
            if (i == position) {
                return key;
            }
            i++;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private BeaconView mCell;

        public ViewHolder(BeaconView v) {
            super(v);
            mCell = v;
        }

        public BeaconView getCell() {
            return mCell;
        }
    }


}