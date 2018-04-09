package doreen.huang.com.sur5ivenavigation;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        listView.setAdapter(new ContactArrayAdapter(view.getContext(), contactList));

        return view;
    }

    public List<Contact> getContacts() {
        List<Contact> contactList = new ArrayList<>();

        // Request.get("http://sur5ive.com/api/contacts");

        Contact contact1 = new Contact();
        contact1.id = 1;
        contact1.firstName = "FTest";
        contact1.lastName = "LTest";
        contact1.phoneNumber = "14165557777";

        contactList.add(contact1);

        Contact contact2 = new Contact();
        contact2.id = 2;
        contact2.firstName = "John";
        contact2.lastName = "Doe";
        contact2.phoneNumber = "14164443333";

        contactList.add(contact2);

        Contact contact3 = new Contact();
        contact3.id = 3;
        contact3.firstName = "John";
        contact3.lastName = "Doe";
        contact3.phoneNumber = "14164443334";

        contactList.add(contact3);

        return contactList;
    }

    private class ContactArrayAdapter extends ArrayAdapter<Contact> {

        public List<Contact> contactList = new ArrayList<>();

        int layoutId;

        ContactArrayAdapter(Context context, List<Contact> objects){
            super(context, R.layout.list_contact, objects);

            this.layoutId = R.layout.list_contact;

            for(int i = 0; i<objects.size(); i++){
                contactList.add(objects.get(i));
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            if( convertView == null ){
                //We must create a View:
                convertView = inflater.inflate(this.layoutId, parent, false);
            }
            //Here we can do changes to the convertView, such as set a text on a TextView
            //or an image on an ImageView.
            TextView lblContactName = convertView.findViewById(R.id.lbl_contactName);
            TextView lblContactNumber = convertView.findViewById(R.id.lbl_contactPhone);

            lblContactName.setText(contactList.get(position).firstName + " " + contactList.get(position).lastName);
            lblContactNumber.setText(contactList.get(position).phoneNumber);

            return convertView;
        }
    }


}