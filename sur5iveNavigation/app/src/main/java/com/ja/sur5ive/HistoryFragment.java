package com.ja.sur5ive;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.ja.sur5ive.models.HistoryItem;
import com.ja.sur5ive.services.SharedPreferencesService;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,
                container, false);

//        List<HistoryItem> historyList = getHistory();

        List<HistoryItem> historyList = SharedPreferencesService.getInstance().getHistory(getActivity());

        ListView listView = view.findViewById(R.id.historyList);

        listView.setAdapter(new HistoryArrayAdapter(view.getContext(), historyList));

        return view;
    }

    public void setText(String text) {
        //TextView view = (TextView) getView().findViewById(R.id.detailsText);
        //view.setText(text);
    }
    public List<HistoryItem> getHistory()
    {
        List<HistoryItem> historyList = new ArrayList<>();

        HistoryItem historyItem1 = new HistoryItem();
        historyItem1.message = "Sent SMS";
        try {
            historyItem1.timestamp = HistoryItem.DATE_FORMAT.parse("1999-07-31 07:32:23");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        historyList.add(historyItem1);

        HistoryItem historyItem2 = new HistoryItem();
        historyItem2.message = "Sent SMS";
        try {
            historyItem2.timestamp = HistoryItem.DATE_FORMAT.parse("2018-04-09 15:02:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        historyList.add(historyItem2);

        return historyList;
    }

    private class HistoryArrayAdapter extends ArrayAdapter<HistoryItem>
    {
        public List<HistoryItem> historyList = new ArrayList<>();

        int layoutID;

        HistoryArrayAdapter (Context context, List<HistoryItem> objects)
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
            TextView lblSentMessage = convertView.findViewById(R.id.lbl_sentmessage);

            lblTimestamp.setText(HistoryItem.DATE_FORMAT.format(historyList.get(position).timestamp));
            lblSentMessage.setText(historyList.get(position).message);

            return convertView;
        }

    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this.getActivity().getApplicationContext(), "Text Field Pressed", Toast.LENGTH_SHORT).show();
    }
}