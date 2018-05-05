package com.beaconify.detect.beaconify.Networking;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequest {
    private OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public HttpRequest () {
        this.client = new OkHttpClient();
    }

    public Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", com.beaconify.detect.beaconify.Cache.token)
                .build();

        return this.client.newCall(request).execute();
    }

    public Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", com.beaconify.detect.beaconify.Cache.token)
                .build();
        
        return this.client.newCall(request).execute();
    }
}
