package com.ja.sur5ive.web;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.ja.sur5ive.Sur5ive;
import com.ja.sur5ive.exception.Sur5iveException;
import com.onehilltech.promises.Promise;
import com.onehilltech.promises.PromiseExecutor;

import static android.content.Context.LOCATION_SERVICE;

public class WebClient {

    private static Sur5ive app = Sur5ive.getInstance();

    public static Promise<String> requestCode(final String phoneNumber) {
        return new Promise<>(new PromiseExecutor<String>() {
            @Override
            public void execute(Promise.Settlement<String> settlement) {

                try {

                    String requestURL = app.DOMAIN + app.REQUEST_CODE_PATH + phoneNumber;

                    URL url;
                    String response = "";

                    url = new URL(requestURL);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    //conn.setDoInput(true);
                    //conn.setDoOutput(true);

                    //conn.getOutputStream().close();

                    int responseCode=conn.getResponseCode();

                    boolean errorOccurred = false;

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line=br.readLine()) != null) {
                            response+=line;
                        }
                    }
                    else {
                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                        while ((line=br.readLine()) != null) {
                            response+=line;
                        }
                        errorOccurred = true;
                    }

                    Log.d("WebClient", "Response: " + response);

                    if(errorOccurred) {
                        throw new Sur5iveException("Invalid Response Code: " + responseCode);
                    }

                    settlement.resolve(response);

                } catch (Exception e) {
                    settlement.reject(e);
                }
            }
        });
    }

    public static Promise<String> validateCode(final String code, final String phoneNumber) {
        return new Promise<>(new PromiseExecutor<String>() {
            @Override
            public void execute(Promise.Settlement<String> settlement) {

                try {
                    String requestURL = app.DOMAIN + app.VALIDATE_CODE_PATH;
                    Map<String, Object> postDataParams = new HashMap<>();

                    URL url;
                    String response = "";

                    url = new URL(requestURL);

                    //TODO: Pull phone number from storage
                    postDataParams.put("PHONE_NUMBER", phoneNumber);
                    postDataParams.put("CODE", code);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();

                    conn.getOutputStream().close();

                    int responseCode = conn.getResponseCode();
                    boolean errorOccurred = false;

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            response += line;
                        }
                    } else {
                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                        while ((line=br.readLine()) != null) {
                            response+=line;
                        }
                        errorOccurred = true;
                    }

                    Log.d("WebClient", "Response: " + response);

                    if (errorOccurred) {
                        throw new Sur5iveException("Invalid Response Code: " + responseCode);
                    }

                    settlement.resolve(response);

                } catch (Exception e) {
                    settlement.reject(e);
                }
            }
        });
    }

    public static Promise<String> sendMessage(final String token, final Map<String, Object> data, final Location location) {
        return new Promise<>(new PromiseExecutor<String>() {
            @Override
            public void execute(Promise.Settlement<String> settlement) {

                try {
                    //  GET /validate/code/<phone number>

                    // POST /validate/validatecode

                    // POST /sms/send?location=latitude,longitude

                    String requestURL = app.DOMAIN + app.SEND_MESSAGE_PATH;

                    if(location != null) {
                        requestURL = requestURL + "?location="+String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude());
                    }

                    URL url;
                    String response = "";

                    url = new URL(requestURL);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", token);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(data));

                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    boolean errorOccurred = false;

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            response += line;
                        }
                    } else {
                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                        while ((line=br.readLine()) != null) {
                            response+=line;
                        }
                        errorOccurred = true;
                    }

                    Log.d("WebClient", "Response: " + response);

                    if (errorOccurred) {
                        throw new Sur5iveException("Invalid Response Code: " + responseCode);
                    }

                    settlement.resolve(response);
                } catch (Exception e) {
                    settlement.reject(e);
                }
            }
        });
    }

    private static String getPostDataString(Map<String, Object> params) throws UnsupportedEncodingException {
        return encodeValue(params);
    }

    private static String encodeValue(Object value) {
        StringBuilder result = new StringBuilder();
        if(value == null) {
            result.append("null");
        } else if(value instanceof Map) {
            result.append("{");
            boolean first = true;
            for (Map.Entry<String, Object> entry : ((Map<String, Object>)value).entrySet()) {

                if (first)
                    first = false;
                else
                    result.append(",");

                result.append("\""+entry.getKey()+"\"");
                result.append(":");
                result.append(encodeValue(entry.getValue()));
            }
            result.append("}");
        } else if(value instanceof List) {
            result.append("[");
            for(Object obj : ((List)value)) {
                result.append(encodeValue(obj));
            }
            result.append("]");
        } else if(value instanceof String) {
            result.append("\"" + value + "\"");
        } else {
            result.append(value.toString());
        }
        return result.toString();
    }


//    private static String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
//        StringBuilder result = new StringBuilder();
//        boolean first = true;
//        for(Map.Entry<String, String> entry : params.entrySet()){
//            if (first)
//                first = false;
//            else
//                result.append("&");
//
//            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//        }
//
//        return result.toString();
//    }

}
