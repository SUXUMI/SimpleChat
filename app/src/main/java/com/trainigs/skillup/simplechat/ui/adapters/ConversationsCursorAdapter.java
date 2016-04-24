package com.trainigs.skillup.simplechat.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trainigs.skillup.simplechat.R;
import com.trainigs.skillup.simplechat.db.ChatContract;

/**
 * Created by Irakli on 4/24/2016
 */
public class ConversationsCursorAdapter extends CursorAdapter {
    public ConversationsCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.conversations_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_conversaton_list_item_image);
        TextView titleTextView = (TextView) view.findViewById(R.id.tv_conversaton_list_item_title);
        TextView lastMessageTextView = (TextView) view.findViewById(R.id.tv_conversaton_list_item_last_message);

        String uri = cursor.getString(cursor.getColumnIndex(ChatContract.Conversation.IMAGE_URI));
        if (!TextUtils.isEmpty(uri)) {
            imageView.setImageURI(Uri.parse(uri));
        } else imageView.setImageResource(R.drawable.user);
        titleTextView.setText(cursor.getString(cursor.getColumnIndex(ChatContract.Conversation.TITLE)));
        lastMessageTextView.setText(cursor.getString(cursor.getColumnIndex(ChatContract.Conversation.LAST_MESSAGE)));
    }
}