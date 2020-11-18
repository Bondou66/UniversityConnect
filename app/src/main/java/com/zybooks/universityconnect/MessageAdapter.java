package com.zybooks.universityconnect;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<DocumentSnapshot> {

    public MessageAdapter(Activity context, ArrayList<DocumentSnapshot> chats) {
        super(context, 0, chats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_message, parent, false);
        }

        DocumentSnapshot current = getItem(position);

        TextView textTextView = (TextView) listItemView.findViewById(R.id.message_text);
        textTextView.setText((String) current.get("Text"));

        TextView userTextView = (TextView) listItemView.findViewById(R.id.message_user);
        userTextView.setText((String) current.get("Username"));

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.message_time);
//        textTextView.setText((Integer) current.get("TimeSent"));

        return listItemView;
    }

}
