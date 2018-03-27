package doreen.huang.com.sur5ivenavigation;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import doreen.huang.com.sur5ivenavigation.models.Contact;

public class ContactsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts,
                container, false);

        List<Contact> contactList = getContacts();

        ListView listView = view.findViewById(R.id.contact_list);

        listView.setAdapter(new ContactArrayAdapter(view.getContext(), 0, contactList));

        return view;
    }

    public List<Contact> getContacts() {
        List<Contact> contactList = new ArrayList<>();

        // Request.get("http://sur5ive.com/api/contacts");

        Contact contact1 = new Contact();
        contact1.id = 1;
        contact1.firstName = "FTest";
        contact1.lastName = "LTest";
        contact1.phoneNumber = "14165618724";

        contactList.add(contact1);

        return contactList;
    }

    private class ContactArrayAdapter extends ArrayAdapter<Contact> {

        List<Contact> contactList = new ArrayList<>();

        ContactArrayAdapter(Context context, int textViewResourceId, List<Contact> objects){
            super(context, textViewResourceId, objects);
            for(int i = 0; i<objects.size(); i++){
                contactList.add(objects.get(i));
            }
        }
    }


}