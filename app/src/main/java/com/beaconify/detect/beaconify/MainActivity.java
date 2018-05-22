package com.beaconify.detect.beaconify;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.beaconify.detect.beaconify.Adapter.ContentListAdapter;
import com.beaconify.detect.beaconify.Helper.JsonHelper;
import com.beaconify.detect.beaconify.Model.Beacons.BeaconResponse;
import com.beaconify.detect.beaconify.Model.Content.Content;
import com.beaconify.detect.beaconify.Model.Content.ContentResponse;
import com.beaconify.detect.beaconify.Networking.HttpRequest;

import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Response;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, NotifyService.class);
        startService(intent);

        ContentRequest contentRequest = new ContentRequest();
        contentRequest.execute();
    }

    private void refresh(ContentResponse content)
    {
        if(content == null)
            return;

        StringBuilder sb = new StringBuilder();
        sb.append("Paper name : ").append(content.getPaperName());
        sb.append("\n");
        sb.append("Paper details : ").append(content.getPaperCode()).append("/").append(content.getStreamCode());
        sb.append("\n");
        sb.append("Room code : ").append(content.getRoomNo());
        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(sb.toString());


        ListView listView = findViewById(R.id.content_list);

        ContentListAdapter adapter = new ContentListAdapter(this, content.getContents(), getApplicationContext());
        listView.setAdapter(adapter);
    }

    private class ContentRequest extends AsyncTask<Void, Void, Response> {

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                HttpRequest httpRequest = new HttpRequest();

                Date now = new Date();
                String timeStamp = new SimpleDateFormat("HHmm").format(now);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                int day = calendar.get(Calendar.DAY_OF_WEEK);

                String contentsEndpoint = MessageFormat.format(AppContext.contentsEndpoint, timeStamp, day);

                return httpRequest.get(AppContext.baseUrl + contentsEndpoint);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response result) {
            if(result == null)
                return;

            try {
                switch (result.code()) {
                    case 200:
                        ContentResponse contentResponse = (ContentResponse) JsonHelper.getInstance().toObject(result.body().string(), ContentResponse.class);
                        refresh(contentResponse);
                        break;
                    case 401:
                        Cache.clear(getApplicationContext());
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        return;
                }
            } catch (Exception e) {
                return;
            }
        }
    }
}
