package doreen.huang.com.sur5ivenavigation;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import doreen.huang.com.sur5ivenavigation.models.History;
import doreen.huang.com.sur5ivenavigation.services.SharedCacheService;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,
                container, false);

        return view;
    }

    public void setText(String text) {
        //TextView view = (TextView) getView().findViewById(R.id.detailsText);
        //view.setText(text);
    }
    public List<History> getHistory()
    {
        List<History> historyList = new ArrayList<>();

        History history1 = new History();
        history1.id = 1;
        history1.message = "Sent SMS";
        try {
            history1.timestamp = DATE_FORMAT.parse("1999-07-31 07:32:23");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        historyList.add(history1);

        History history2 = new History();
        history2.id = 2;
        history2.message = "Sent SMS";
        try {
            history2.timestamp = DATE_FORMAT.parse("2018-04-09 15:02:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        historyList.add(history2);

        return historyList;
    }

    private class historyArrayAdapter extends ArrayAdapter<History>
    {
        public List<History> historyList = new ArrayList<>();

        int layoutID;

        historyArrayAdapter (Context context, List<History> objects)
        {
            super(context, R.layout.list_history, objects);

            this.layoutID = R.layout.list_history;

            for(int i = 0; i<objects.size(); i++){
                historyList.add(objects.get(i));
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            if( convertView == null ){
                //We must create a View:
                convertView = inflater.inflate(this.layoutID, parent, false);
            }
            //Here we can do changes to the convertView, such as set a text on a TextView
            //or an image on an ImageView.
            TextView lblTimestamp = convertView.findViewById(R.id.lbl_timestamp);

            lblTimestamp.setText(DATE_FORMAT.format(historyList.get(position).timestamp));

            return convertView;
        }

    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this.getActivity().getApplicationContext(), "Text Field Pressed", Toast.LENGTH_SHORT).show();
    }
}