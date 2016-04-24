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

/**
 * Created by Irakli on 4/22/2016
 */
public class ContactsCursorAdapter extends CursorAdapter {
    public ContactsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.contacts_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_contacts_list_item_image);
        TextView nameTextView = (TextView) view.findViewById(R.id.tv_contacts_list_item_name);
        TextView numberTextView = (TextView) view.findViewById(R.id.tv_iv_contacts_list_item_number);

        nameTextView.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
        numberTextView.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))))
            imageView.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))));
        else imageView.setImageResource(R.drawable.user);
    }
}