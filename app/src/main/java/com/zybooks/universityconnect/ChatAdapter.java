package com.zybooks.universityconnect;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<DocumentSnapshot> {

    public ChatAdapter(Activity context, ArrayList<DocumentSnapshot> chats) {
        super(context, 0, chats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_chat, parent, false);
        }

        DocumentSnapshot current = getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.chat_name);

        nameTextView.setText(current.getId());

        return listItemView;


    }

}
