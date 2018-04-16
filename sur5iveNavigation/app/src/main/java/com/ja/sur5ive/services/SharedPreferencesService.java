package com.ja.sur5ive.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ja.sur5ive.R;
import com.ja.sur5ive.models.Contact;
import com.ja.sur5ive.models.HistoryItem;
import com.ja.sur5ive.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesService {

    private static SharedPreferencesService instance = new SharedPreferencesService();

    public static SharedPreferencesService getInstance() {
        return instance;
    }

    public User getUser(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);

        Boolean userExists = sharedPref.getBoolean(context.getString(R.string.preference_usersetup), false);

        if(userExists) {
            User user = new User();
            user.firstName = sharedPref.getString(context.getString(R.string.preference_firstname), null);
            user.lastName = sharedPref.getString(context.getString(R.string.preference_lastname), null);
            user.phoneNumber = sharedPref.getString(context.getString(R.string.preference_phonenumber), null);

            return user;
        }

        return null;
    }

    public void saveUser(Context context, User user) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.preference_firstname), user.firstName);
        editor.putString(context.getString(R.string.preference_lastname), user.lastName);
        editor.putString(context.getString(R.string.preference_phonenumber), user.phoneNumber);
        editor.commit();
    }

    public List<Contact> getContacts(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);

        String strContactsJson = sharedPref.getString(context.getString(R.string.preference_contacts), null);

        List<Contact> contactList = new ArrayList<>();
        if(strContactsJson != null) {
            try {
                JSONArray contactJsonArray = new JSONArray(strContactsJson);
                for(int i = 0; i < contactJsonArray.length() ; i++) {
                    JSONObject contactJson = contactJsonArray.getJSONObject(i);
                    Contact contact = new Contact();
                    contact.id = contactJson.getString("id");
                    contact.firstName = contactJson.getString("firstName");
                    contact.lastName = contactJson.getString("lastName");
                    contact.phoneNumber = contactJson.getString("phoneNumber");
                    contactList.add(contact);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return contactList;
    }

    public void saveContacts(Context context, List<Contact> contactList) {
        JSONArray contactJsonArray = new JSONArray();
        for(Contact contact : contactList) {
            JSONObject contactJson = new JSONObject();
            try {
                contactJson.put("id", contact.id);
                contactJson.put("firstName", contact.firstName);
                contactJson.put("lastName", contact.lastName);
                contactJson.put("phoneNumber", contact.phoneNumber);
                contactJsonArray.put(contactJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.preference_contacts), contactJsonArray.toString());
        editor.commit();
    }

    public List<HistoryItem> getHistory(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);

        String strHistoryJson = sharedPref.getString(context.getString(R.string.preference_history), null);

        List<HistoryItem> historyList = new ArrayList<>();
        if(strHistoryJson != null) {
            try {
                JSONArray historyJsonArray = new JSONArray(strHistoryJson);
                for(int i = 0; i < historyJsonArray.length() ; i++) {
                    JSONObject historyJson = historyJsonArray.getJSONObject(i);
                    HistoryItem historyItem = new HistoryItem();
                    historyItem.timestamp = HistoryItem.DATE_FORMAT.parse(historyJson.getString("timestamp"));
                    historyItem.message = historyJson.getString("message");
                    historyList.add(historyItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return historyList;
    }

    public void saveHistory(Context context, List<HistoryItem> historyList) {
        JSONArray contactJsonArray = new JSONArray();
        for(HistoryItem historyItem : historyList) {
            JSONObject historyItemJson = new JSONObject();
            try {
                historyItemJson.put("timestamp", HistoryItem.DATE_FORMAT.format(historyItem.timestamp));
                historyItemJson.put("message", historyItem.message);
                contactJsonArray.put(historyItemJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.preference_history), contactJsonArray.toString());
        editor.commit();
    }
}
