package com.ja.sur5ive.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ja.sur5ive.ContactsFragment;
import com.ja.sur5ive.MainActivity;
import com.ja.sur5ive.MessageFragment;
import com.ja.sur5ive.R;
import com.ja.sur5ive.exception.Sur5iveException;
import com.ja.sur5ive.web.WebClient;
import com.onehilltech.promises.Promise;
import com.onehilltech.promises.RejectedOnUIThread;
import com.onehilltech.promises.ResolvedOnUIThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PinActivity extends AppCompatActivity {

    private TextView inputPin;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        inputPin = (TextView) findViewById(R.id.pinView);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Button prevButton = findViewById(R.id.previous_button);
        Button nextButton = findViewById(R.id.next_button);

        final Activity activity = this;

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditUserActivity.class);
                intent.putExtra("newUser", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
                activity.finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private boolean validatePin() {

        String pin = inputPin.getText().toString().trim();

        if (pin.isEmpty() || pin.length() != 4) {
            inputPin.setError(getString(R.string.err_invalid_value));
            requestFocus(inputPin);
            return false;
        } else {

        }

        return true;
    }



    /**
     * Validating form
     */
    private void submitForm() {
        if (!validatePin()) {
            return;
        }

        boolean errorOccurred = false;
        String response = null;
        String token = null;

        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);

        String phoneNumber = sharedPref.getString(getString(R.string.preference_phonenumber), null);

        final Promise<String> validateCodePromise = WebClient.validateCode(inputPin.getText().toString(), phoneNumber);

        validateCodePromise.then(ResolvedOnUIThread.onUiThread(new Promise.OnResolved<String,Object>() {
            @Override
            public Promise<Object> onResolved(String s) {

                boolean errorOccurred = false;
                String token = null;
                String response = s;

                try {
                    JSONObject jObject = new JSONObject(response);
                    token = jObject.getString("token");
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "An error occurred while validating PIN, please check your PIN, or try again later.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    errorOccurred = true;
                }

                if(!errorOccurred) {
                    Toast.makeText(getApplicationContext(), "PIN validation successful!.", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.preference_token), token);
                    editor.putBoolean(getString(R.string.preference_pinsetup), true);
                    editor.commit();

                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(intent);
                    activity.finish();
                }

                return Promise.resolve(null);
            }
        }), RejectedOnUIThread.onUiThread(new Promise.OnRejected() {
            @Override
            public Promise onRejected(Throwable throwable) {
                Toast.makeText(getApplicationContext(), "An error occurred while validating PIN, please try again later.", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
                return Promise.reject(throwable);
            }
        }));
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
