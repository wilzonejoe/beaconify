package com.beaconify.detect.beaconify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.BeaconManager.MonitoringListener;
import com.aprilbrother.aprilbrothersdk.Region;
import com.beaconify.detect.beaconify.Helper.JsonHelper;
import com.beaconify.detect.beaconify.Model.Beacons.BeaconResponse;
import com.beaconify.detect.beaconify.Networking.HttpRequest;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Response;

public class NotifyService extends Service {
    private final static String sample_url = "http://codeversed.com/androidifysteve.png";
    private BeaconManager beaconManager;
    private static final Region ALL_BEACONS_REGION = new Region("apr", null,
            null, null);
    private static NotificationManager mNotificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        beaconManager = new BeaconManager(this);
        startMonitoring();
        super.onCreate();
    }

    private void startMonitoring() {
        beaconManager.setMonitoringExpirationMill(20L);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startMonitoring(ALL_BEACONS_REGION);
                } catch (RemoteException e) {

                }
            }
        });

        beaconManager.setMonitoringListener(new MonitoringListener() {

            @Override
            public void onExitedRegion(Region region) {
                //do nothing
            }

            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                generateNotificationContext(beacons);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void generateNotificationContext(List<Beacon> beacons) {
        BeaconRequest br = new BeaconRequest(beacons);
        br.execute();
    }

    public void showNotification(Context context, String title, String body) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";

        int importance = NotificationManager.IMPORTANCE_HIGH;
        Intent resultIntent = new Intent(this, ResultActivity.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    private class BeaconRequest extends AsyncTask<Void, Void, Response> {

        private List<Beacon> beaconUUIDs;
        public BeaconRequest(List<Beacon> beaconUUIDs){
            this.beaconUUIDs = beaconUUIDs;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                HttpRequest httpRequest = new HttpRequest();

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < beaconUUIDs.size(); i++) {
                    sb.append(beaconUUIDs.get(i).getProximityUUID());
                    if((i+1) < beaconUUIDs.size())
                        sb.append(",");
                }

                Date now = new Date();

                String timeStamp = new SimpleDateFormat("HHmm").format(now);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                int day = calendar.get(Calendar.DAY_OF_WEEK);

                String beaconsEndpoint = MessageFormat.format(AppContext.beaconsEndpoint, sb.toString(), timeStamp, day);

                return httpRequest.get(AppContext.baseUrl + beaconsEndpoint);
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
                        BeaconResponse beaconResponse = (BeaconResponse) JsonHelper.getInstance().toObject(result.body().string(), BeaconResponse.class);
                        if (beaconResponse.getClassRoom() != null && beaconResponse.getClassRoom().size() > 0)
                            showNotification(getApplicationContext(), beaconResponse.getClassRoom().get(0).getRoomNo(), "Welcome to " + beaconResponse.getClassRoom().get(0).getPaperName());
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
