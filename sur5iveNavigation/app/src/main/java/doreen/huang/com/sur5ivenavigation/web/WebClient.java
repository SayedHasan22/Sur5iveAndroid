package doreen.huang.com.sur5ivenavigation.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import doreen.huang.com.sur5ivenavigation.Sur5ive;
import doreen.huang.com.sur5ivenavigation.services.SharedCacheService;

public class WebClient {

    private static Sur5ive app = Sur5ive.getInstance();

    public static void requestCode() throws IOException {
        String requestURL = app.DOMAIN + app.REQUEST_CODE_PATH;

        URL url;
        String response = "";

        url = new URL(requestURL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.getOutputStream().close();

        int responseCode=conn.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }
        }
        else {
            response="";

        }
    }

    public static String validateCode(String code) throws IOException {
        String requestURL = app.DOMAIN + app.VALIDATE_CODE_PATH;
        Map<String, Object> postDataParams = new HashMap<>();

        URL url;
        String response = "";

        url = new URL(requestURL);

        //TODO: Pull phone number from storage
        postDataParams.put("PHONE_NUMBER", SharedCacheService.getInstance().getCache().get("phone_number"));
        postDataParams.put("CODE", code);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
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

        int responseCode=conn.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }
        }
        else {
            response="";

        }

        return response;
    }

    public static void sendMessage() throws IOException {

        //  GET /validate/code/<phone number>

        // POST /validate/validatecode

        // POST /sms/send?location=latitude,longitude

        String requestURL = app.DOMAIN + app.SEND_MESSAGE_PATH;
        Map<String,Object> postDataParams = new HashMap<String,Object>();

        URL url;
        String response = "";

        url = new URL(requestURL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);


        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(postDataParams));

        writer.flush();
        writer.close();
        os.close();
        int responseCode=conn.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }
        }
        else {
            response="";

        }
    }

    private static String getPostDataString(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        result.append("{");

        boolean first = true;
        for(Map.Entry<String, Object> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append(",");

            result.append("\""+entry.getKey()+"\"");
            result.append("=");
            if(entry.getValue() instanceof String) {
                result.append("\"" + entry.getValue().toString() + "\"");
            } else {
                result.append(entry.getValue().toString());
            }
        }

        result.append("}");

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
