package com.ja.sur5ive;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EmergencyFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_old,
                container, false);

        TextView txtNumber = view.findViewById(R.id.saveButton);
        txtNumber.setOnClickListener(this);
        txtNumber.getText();

        List<Object> contactList = new ArrayList<>();

        // Add to cache
        //SharedCacheService.getInstance().getCache().put("ContactList", contactList);

        // Get from cache
        //contactList = (List<Object>) SharedCacheService.getInstance().getCache().get("ContactList");

        return view;
    }

    public void setText(String text) {
        //TextView view = (TextView) getView().findViewById(R.id.detailsText);
        //view.setText(text);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this.getActivity().getApplicationContext(), "Text Field Pressed", Toast.LENGTH_SHORT).show();
    }
}