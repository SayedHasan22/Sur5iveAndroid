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

import com.ja.sur5ive.R;
import com.ja.sur5ive.models.Contact;
import com.ja.sur5ive.services.SharedPreferencesService;
import com.ja.sur5ive.web.WebClient;
import com.onehilltech.promises.Promise;
import com.onehilltech.promises.RejectedOnUIThread;
import com.onehilltech.promises.ResolvedOnUIThread;

import java.util.List;
import java.util.UUID;

public class EditContactActivity extends AppCompatActivity {

    private EditText inputFirstName, inputLastName, inputPhoneNumber;
    private TextInputLayout inputLayoutFirstName, inputLayoutLastName, inputLayoutPhoneNumber;
    private Button btnNextOrSave;
    private Activity activity = this;

    Contact contact = new Contact();

    List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        String contactId = this.getIntent().getStringExtra("contactId");

        contactList = SharedPreferencesService.getInstance().getContacts(this);

        if(contactId != null) {
            boolean found = false;
            for(Contact currentContact : contactList) {
                if(currentContact.id.equals(contactId)) {
                    contact = currentContact;
                    found = true;
                    break;
                }
            }
            if(!found) {
                Toast.makeText(this, "Contact not found!", Toast.LENGTH_SHORT).show();
                Intent emptyIntent = new Intent();
                this.setResult(RESULT_CANCELED, emptyIntent);
                finish();
            }
        } else {
            contact.id = UUID.randomUUID().toString();
        }

        inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_firstname);
        inputLayoutLastName = (TextInputLayout) findViewById(R.id.input_layout_lastname);
        inputLayoutPhoneNumber = (TextInputLayout) findViewById(R.id.input_layout_phonenumber);
        inputFirstName = (EditText) findViewById(R.id.input_firstname);
        inputLastName = (EditText) findViewById(R.id.input_lastname);
        inputPhoneNumber = (EditText) findViewById(R.id.input_phonenumber);
        btnNextOrSave = (Button) findViewById(R.id.next_button);

        inputFirstName.setText(contact.firstName);
        inputLastName.setText(contact.lastName);
        inputPhoneNumber.setText(contact.phoneNumber);

        btnNextOrSave.setText("Save");

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

        contact.firstName = inputFirstName.getText().toString().trim();
        contact.lastName = inputLastName.getText().toString().trim();
        contact.phoneNumber = inputPhoneNumber.getText().toString().trim();

        if(!contactList.contains(contact)) {
            contactList.add(contact);
        }

        SharedPreferencesService.getInstance().saveContacts(this, contactList);

        Toast.makeText(this, "Contact saved successfully!", Toast.LENGTH_SHORT);

        Intent result = new Intent();
        this.setResult(RESULT_OK, result);
        finish();
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
