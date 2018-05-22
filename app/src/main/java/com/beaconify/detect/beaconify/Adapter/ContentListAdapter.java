package com.beaconify.detect.beaconify.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.beaconify.detect.beaconify.Model.Content.Content;
import com.beaconify.detect.beaconify.R;

import java.util.List;

public class ContentListAdapter extends ArrayAdapter<Content>{

    private List<Content> dataSet;
    private Context mContext;
    private Activity activity;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
    }

    public ContentListAdapter(Activity activity, List<Content> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Content dataModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.content_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(dataModel.getTitle());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataModel.getUrl()));
                activity.startActivity(browserIntent);
            }
        });
        return convertView;
    }
}