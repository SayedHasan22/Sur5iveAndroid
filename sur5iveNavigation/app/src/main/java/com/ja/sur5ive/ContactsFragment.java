package com.ja.sur5ive;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ja.sur5ive.activities.EditContactActivity;
import com.ja.sur5ive.activities.EditUserActivity;
import com.ja.sur5ive.models.Contact;
import com.ja.sur5ive.services.SharedPreferencesService;

public class ContactsFragment extends Fragment {

    private int ADD_CONTACT_REQUEST = 1;
    private int EDIT_CONTACT_REQUEST = 2;

    List<Contact> contactList;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts,
                container, false);

        contactList = SharedPreferencesService.getInstance().getContacts(getActivity());

        setHasOptionsMenu(true);

        listView = view.findViewById(R.id.contact_list);

        listView.setAdapter(new ContactArrayAdapter(view.getContext(), contactList));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contacts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add :
                Intent intent = new Intent(getActivity(), EditContactActivity.class);
                startActivityForResult(intent, ADD_CONTACT_REQUEST);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_CONTACT_REQUEST || requestCode == EDIT_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // The user has been modified
                contactList = SharedPreferencesService.getInstance().getContacts(getActivity());

                ((ContactArrayAdapter)listView.getAdapter()).setContactList(contactList);

                ((ContactArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        }
    }

    public List<Contact> getContacts() {
        List<Contact> contactList = new ArrayList<>();

        // Request.get("http://sur5ive.com/api/contacts");

        Contact contact1 = new Contact();
        contact1.id = UUID.randomUUID().toString();
        contact1.firstName = "FTest";
        contact1.lastName = "LTest";
        contact1.phoneNumber = "14165557777";

        contactList.add(contact1);

        Contact contact2 = new Contact();
        contact2.id = UUID.randomUUID().toString();
        contact2.firstName = "John";
        contact2.lastName = "Doe";
        contact2.phoneNumber = "14164443333";

        contactList.add(contact2);

        Contact contact3 = new Contact();
        contact3.id = UUID.randomUUID().toString();
        contact3.firstName = "John";
        contact3.lastName = "Doe";
        contact3.phoneNumber = "14164443334";

        contactList.add(contact3);

        return contactList;
    }

    private class ContactArrayAdapter extends ArrayAdapter<Contact> {

        private List<Contact> contactList = new ArrayList<>();

        int layoutId;

        ContactArrayAdapter(Context context, List<Contact> objects){
            super(context, R.layout.list_contact, objects);

            this.layoutId = R.layout.list_contact;

            this.contactList = objects;
        }

        public void setContactList(List<Contact> contactList) {
            this.contactList.clear();
            this.contactList.addAll(contactList);
        }

        public List<Contact> getContactList() {
            return contactList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            final Contact contact = contactList.get(position);

            if( convertView == null ){
                //We must create a View:
                convertView = inflater.inflate(this.layoutId, parent, false);
            }
            //Here we can do changes to the convertView, such as set a text on a TextView
            //or an image on an ImageView.
            TextView lblContactName = convertView.findViewById(R.id.lbl_contactName);
            TextView lblContactNumber = convertView.findViewById(R.id.lbl_contactPhone);
            ImageButton btnDelete = convertView.findViewById(R.id.button_delete);

            lblContactName.setText(contact.firstName + " " + contact.lastName);
            lblContactNumber.setText(contact.phoneNumber);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactList.remove(contact);

                    SharedPreferencesService.getInstance().saveContacts(getActivity(), contactList);

                    notifyDataSetChanged();
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EditContactActivity.class);
                    intent.putExtra("contactId", contact.id);
                    startActivityForResult(intent, EDIT_CONTACT_REQUEST);
                }
            });

            return convertView;
        }
    }


}