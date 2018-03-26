package doreen.huang.com.sur5ivenavigation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import doreen.huang.com.sur5ivenavigation.services.SharedCacheService;

public class ContactsFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts,
                container, false);

        List<Object> contactList = new ArrayList<>();

        

        return view;
    }

    private class ContactArrayAdapter extends ArrayAdapter<Contact> {

        List<String> contactList = new ArrayList<Contact>();

        public ContactArrayAdapter(Context context, int textViewResourceId, List<Contact> objects){
            super(context, textViewResourceId, objects)
            for(int i = 0; i<objects.size(); i++){
                contactList.put(objects.get(i));
            }
        }
    }


}