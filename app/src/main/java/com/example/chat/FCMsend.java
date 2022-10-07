package com.example.chat;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMsend {

    private static final String sBASE_URL="https://fcm.googleapis.com/fcm/send" ;
    private static final String S_SERVER_KEY="key=AAAAQhiAj4Q:APA91bHVT9RrOhai4YGSv-01icCSdIYgp2UCLW6n9iV3hEGxfSn7qblTrBlfvE99fbDPChBethaAyIEolIXbWxC7nXA_GgGjBBUgJnx5lkNmJJ6YNwYjzM02Fdmk-MYf_-ch5f8kHQqp";
    public static void pushNotification(Context context,String token,String title,String message)
    {

        StrictMode.ThreadPolicy policy  = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(context);
        {
            try {
                JSONObject mainobj =new JSONObject();
                mainobj.put("to",token);
                JSONObject notificationObj = new JSONObject();
                notificationObj.put("title",title);
                notificationObj.put("body",message);
                mainobj.put("notificatio" +
                        "n",notificationObj);
                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, sBASE_URL, mainobj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(context.getApplicationContext(),""+ response,Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context.getApplicationContext(),"error "+ error,Toast.LENGTH_SHORT).show();
                        System.out.println(error);
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> header =new HashMap<>();
                        header.put("Content-Type","application/json");
                        header.put("Authorization",S_SERVER_KEY);
                        return header;
                    }
                };
                queue.add(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}