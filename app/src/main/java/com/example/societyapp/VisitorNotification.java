package com.example.societyapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VisitorNotification {

    final String SERVER_KEY = "key="+"AAAA50A3L3s:APA91bHUNIXZisyVkYtw0DWYmw64T45UmImcugcsrcMv_caSiZSjefuk3T62B5Oeq7vx60KRLtAnMXedLWdjlPbJm_zgoMsViraEuijFCwFrUFVAZXAwgwSBKt2DmDm0iH54KrY-6pVq";
    final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    final private String contentType = "application/json";
    JSONObject notification,notificationBody;
    private Context ctx;

    public VisitorNotification(Context ctx,String title,String message,String topic) throws JSONException {
        notification = new JSONObject();
        notificationBody = new JSONObject();
        notificationBody.put("type","g2v");
        notificationBody.put("title",title);
        notificationBody.put("message",message);
        notification.put("to","/topics/"+topic);
        notification.put("data",notificationBody);
        this.ctx = ctx;
    }

    public void sendNotification() throws Exception {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(API_URL_FCM, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("tag", "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("tag2", "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", SERVER_KEY);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }
}
