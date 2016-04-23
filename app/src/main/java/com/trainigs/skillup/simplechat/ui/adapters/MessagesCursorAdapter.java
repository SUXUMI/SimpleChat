package com.trainigs.skillup.simplechat.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trainigs.skillup.simplechat.R;
import com.trainigs.skillup.simplechat.db.ChatContract;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Irakli on 4/22/2016
 */
public class MessagesCursorAdapter extends CursorAdapter {
    SimpleDateFormat simpleDateFormat;

    public MessagesCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        simpleDateFormat = new SimpleDateFormat("mm:hh dd-MMM-yyyy");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.messages_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        LinearLayout incomingLayout = (LinearLayout) view.findViewById(R.id.messages_list_item_incoming_layout);
        LinearLayout outgoingLayout = (LinearLayout) view.findViewById(R.id.messages_list_item_outgoing_layout);
        TextView incomingContentTextView = (TextView) view.findViewById(R.id.tv_message_list_item_incoming_content);
        TextView outgoingContentTextView = (TextView) view.findViewById(R.id.tv_message_list_item_outgoing_content);
        TextView incomingDateTextView = (TextView) view.findViewById(R.id.tv_message_list_item_incoming_date);
        TextView outgoingDateTextView = (TextView) view.findViewById(R.id.tv_message_list_item_outgoing_date);

        int type = cursor.getInt(cursor.getColumnIndex(ChatContract.Message.TYPE));
        if (type == 0) {
            outgoingLayout.setVisibility(View.GONE);
            incomingLayout.setVisibility(View.VISIBLE);
            incomingContentTextView.setText(cursor.getString(cursor.getColumnIndex(ChatContract.Message.CONTENT)));
            Date date = new Date(cursor.getInt(cursor.getColumnIndex(ChatContract.Message.DATE)));
            incomingDateTextView.setText(simpleDateFormat.format(date));
        } else {
            outgoingLayout.setVisibility(View.VISIBLE);
            incomingLayout.setVisibility(View.GONE);
            outgoingContentTextView.setText(cursor.getString(cursor.getColumnIndex(ChatContract.Message.CONTENT)));
            Date date = new Date(cursor.getInt(cursor.getColumnIndex(ChatContract.Message.DATE)));
            outgoingDateTextView.setText(simpleDateFormat.format(date));
        }
    }
}