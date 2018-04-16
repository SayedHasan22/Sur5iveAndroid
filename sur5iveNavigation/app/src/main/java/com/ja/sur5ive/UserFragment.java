package com.ja.sur5ive;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ja.sur5ive.activities.EditUserActivity;
import com.ja.sur5ive.activities.PinActivity;
import com.ja.sur5ive.models.User;
import com.ja.sur5ive.services.SharedPreferencesService;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private int EDIT_USER_REQUEST = 1;

    private EditText txtFirstName, txtLastName, txtPhoneNumber;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,
                container, false);

        txtFirstName = view.findViewById(R.id.input_firstname);
        txtLastName = view.findViewById(R.id.input_lastname);
        txtPhoneNumber = view.findViewById(R.id.input_phonenumber);

        updateUser();

        setHasOptionsMenu(true);

        return view;
    }

    private void updateUser() {
        user = SharedPreferencesService.getInstance().getUser(getActivity());

        txtFirstName.setText(user.firstName);
        txtLastName.setText(user.lastName);
        txtPhoneNumber.setText(user.phoneNumber);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit :
                Intent intent = new Intent(getActivity(), EditUserActivity.class);
                intent.putExtra("newUser", false);
                startActivityForResult(intent, EDIT_USER_REQUEST);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EDIT_USER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // The user has been modified
                updateUser();
            }
        }
    }

}