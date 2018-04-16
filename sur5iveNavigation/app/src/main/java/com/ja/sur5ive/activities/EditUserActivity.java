package com.ja.sur5ive.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ja.sur5ive.MainActivity;
import com.ja.sur5ive.R;
import com.ja.sur5ive.exception.Sur5iveException;
import com.ja.sur5ive.models.User;
import com.ja.sur5ive.services.SharedPreferencesService;
import com.ja.sur5ive.web.WebClient;
import com.onehilltech.promises.Promise;
import com.onehilltech.promises.ResolvedOnUIThread;
import com.onehilltech.promises.RejectedOnUIThread;

import java.io.IOException;
import java.util.concurrent.Future;

public class EditUserActivity extends AppCompatActivity {

    private EditText inputFirstName, inputLastName, inputPhoneNumber;
    private TextInputLayout inputLayoutFirstName, inputLayoutLastName, inputLayoutPhoneNumber;
    private Button btnNextOrSave;
    private Activity activity = this;

    private boolean isNewUser = false;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        isNewUser = this.getIntent().getBooleanExtra("newUser", true);

        TextView txtPin = (TextView) findViewById(R.id.pinView);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_firstname);
        inputLayoutLastName = (TextInputLayout) findViewById(R.id.input_layout_lastname);
        inputLayoutPhoneNumber = (TextInputLayout) findViewById(R.id.input_layout_phonenumber);
        inputFirstName = (EditText) findViewById(R.id.input_firstname);
        inputLastName = (EditText) findViewById(R.id.input_lastname);
        inputPhoneNumber = (EditText) findViewById(R.id.input_phonenumber);
        btnNextOrSave = (Button) findViewById(R.id.next_button);

        user = SharedPreferencesService.getInstance().getUser(this);

        if (user != null) {
            inputFirstName.setText(user.firstName);
            inputLastName.setText(user.lastName);
            inputPhoneNumber.setText(user.phoneNumber);
        } else {
            user = new User();
        }

        if(isNewUser) {
            btnNextOrSave.setText("Next");
        } else {
            btnNextOrSave.setText("Save");
        }

        btnNextOrSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateFirstName()) {
            return;
        }

        if (!validateLastName()) {
            return;
        }

        if (!validatePhoneNumber()) {
            return;
        }

        String phoneNumber = inputPhoneNumber.getText().toString().trim();
        boolean errorOccurred = false;

        String currentPhoneNumber = inputPhoneNumber.getText().toString().trim();

        boolean phoneNumberChanged = user.phoneNumber == null || !user.phoneNumber.equals(currentPhoneNumber);

        if(phoneNumberChanged) {
            final Promise<String> requestCodePromise = WebClient.requestCode(phoneNumber);

            requestCodePromise.then(ResolvedOnUIThread.onUiThread(new Promise.OnResolved<String, Object>() {
                @Override
                public Promise<Object> onResolved(String s) {
                    Toast.makeText(getApplicationContext(), "SMS sent to phone number.", Toast.LENGTH_SHORT).show();

                    user.firstName = inputFirstName.getText().toString().trim();
                    user.lastName = inputLastName.getText().toString().trim();
                    user.phoneNumber = inputPhoneNumber.getText().toString().trim();

                    SharedPreferencesService.getInstance().saveUser(activity, user);

                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.preference_usersetup), true);
                    editor.putBoolean(getString(R.string.preference_pinsetup), false);
                    editor.commit();

                    Intent intent = new Intent(activity, PinActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(intent);
                    activity.finish();

                    return Promise.resolve(null);
                }
            }), RejectedOnUIThread.onUiThread(new Promise.OnRejected() {
                @Override
                public Promise onRejected(Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "An error occurred while requesting SMS, please try again later.", Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                    return Promise.reject(throwable);
                }
            }));
        } else {
            user.firstName = inputFirstName.getText().toString().trim();
            user.lastName = inputLastName.getText().toString().trim();
            user.phoneNumber = inputPhoneNumber.getText().toString().trim();

            SharedPreferencesService.getInstance().saveUser(activity, user);

            if(isNewUser) {
                Intent intent = new Intent(activity, PinActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
            } else {
                Intent result = new Intent();
                setResult(RESULT_OK, result);
            }
            finish();
        }
    }

    private boolean validateFirstName() {
        if (inputFirstName.getText().toString().trim().isEmpty()) {
            inputLayoutFirstName.setError(getString(R.string.err_empty_field));
            requestFocus(inputFirstName);
            return false;
        } else {
            inputLayoutFirstName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLastName() {
        String lastName = inputLastName.getText().toString().trim();

        if (lastName.isEmpty()) {
            inputLayoutLastName.setError(getString(R.string.err_empty_field));
            requestFocus(inputLastName);
            return false;
        } else {
            inputLayoutLastName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhoneNumber() {
        if (inputPhoneNumber.getText().toString().trim().isEmpty()) {
            inputLayoutPhoneNumber.setError(getString(R.string.err_empty_field));
            requestFocus(inputPhoneNumber);
            return false;
        } else {
            inputLayoutPhoneNumber.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class UserTextWatcher implements TextWatcher {

        private View view;

        private UserTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_firstname:
                    validateFirstName();
                    break;
                case R.id.input_lastname:
                    validateLastName();
                    break;
                case R.id.input_phonenumber:
                    validatePhoneNumber();
                    break;
            }
        }
    }
}
