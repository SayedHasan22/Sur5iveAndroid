package com.ja.sur5ive;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ja.sur5ive.models.Contact;
import com.ja.sur5ive.models.HistoryItem;
import com.ja.sur5ive.models.User;
import com.ja.sur5ive.services.SharedPreferencesService;
import com.ja.sur5ive.web.WebClient;
import com.onehilltech.promises.Promise;
import com.onehilltech.promises.RejectedOnUIThread;
import com.onehilltech.promises.ResolvedOnUIThread;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class MessageFragment extends Fragment {

    private Button btnCheckIn, btnCheckOut, btnOnRoute, btnAvailable;

    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,
                container, false);

        btnCheckIn = view.findViewById(R.id.button_check_in);
        btnCheckOut = view.findViewById(R.id.button_check_out);
        btnOnRoute = view.findViewById(R.id.button_on_route);
        btnAvailable = view.findViewById(R.id.button_available);

        activity = (MainActivity) getActivity();

        //Initializing buttons

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("Checking In. ");
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("Checking Out. ");
            }
        });

        btnOnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("On Route. ");
            }
        });

        btnAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("Available. ");
            }
        });

        return view;
    }

    private void sendMessage(final String message) {
        Map<String,Object> data = prepareMessage(message);

        final SharedPreferences sharedPrefs = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPrefs.getString(getString(R.string.preference_token), null);

        WebClient.sendMessage(token, data, activity.lastKnownLocation).then(ResolvedOnUIThread.onUiThread(new Promise.OnResolved<String, Object>() {

            @Override
            public Promise<Object> onResolved(String s) {
                Toast.makeText(getActivity(), "Message sent!", Toast.LENGTH_SHORT);

                List<HistoryItem> historyItemList = SharedPreferencesService.getInstance().getHistory(getActivity());

                HistoryItem historyItem = new HistoryItem();

                historyItem.timestamp = new Date();
                historyItem.message = message;

                historyItemList.add(historyItem);

                SharedPreferencesService.getInstance().saveHistory(getActivity(), historyItemList);

                return Promise.resolve(null);
            }
        }),RejectedOnUIThread.onUiThread(new Promise.OnRejected() {

            @Override
            public Promise onRejected(Throwable throwable) {
                Toast.makeText(getActivity(), "An error occurred while requesting SMS, please try again later.", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
                return Promise.reject(throwable);
            }
        }));
    }

    private Map<String,Object> prepareMessage(String message) {
        SharedPreferencesService sharedPreferencesService = SharedPreferencesService.getInstance();
        List<Contact> contactList = sharedPreferencesService.getContacts(getActivity());
        User user = sharedPreferencesService.getUser(getActivity());
        Map<String,Object> data = new HashMap<>();

        Map<String,Object> userMap = new HashMap<>();
        userMap.put("FIRST_NAME", user.firstName);
        userMap.put("LAST_NAME", user.lastName);
        userMap.put("PHONE_NUMBER", user.phoneNumber);
        userMap.put("MESSAGE", message);

        data.put("USER", userMap);

        List<Map<String,Object>> contactMapList = new ArrayList<>();
        for(Contact contact : contactList) {
            Map<String,Object> contactMap = new HashMap<>();
            contactMap.put("FIRST_NAME",contact.firstName);
            contactMap.put("LAST_NAME",contact.lastName);
            contactMap.put("PHONE_NUMBER",contact.phoneNumber);
            contactMapList.add(contactMap);
        }

        data.put("CONTACTS",contactMapList);

        return data;
    }
}