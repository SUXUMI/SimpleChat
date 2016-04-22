package com.trainigs.skillup.simplechat.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trainigs.skillup.simplechat.R;

/**
 * Created by ADMIN on 16.04.2016
 */
public class ContactsAdapter extends RecyclerViewCursorAdapter<ContactsAdapter.MyViewHolder> {
    /**
     * Constructor.
     *
     * @param context The Context the Adapter is displayed in.
     */
    public ContactsAdapter(Context context) {
        super(context);
        setupCursorAdapter(null, 0, R.layout.contacts_list_item, false);
    }

    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.MyViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);

        setViewHolder(holder);

        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }

    public static class MyViewHolder extends RecyclerViewCursorViewHolder {

        ImageView imageView;
        TextView nameTextView;
        TextView numberTextView;

        /**
         * Constructor.
         *
         * @param view The root view of the ViewHolder.
         */
        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iv_contacts_list_item_image);
            nameTextView = (TextView) view.findViewById(R.id.tv_contacts_list_item_name);
            numberTextView = (TextView) view.findViewById(R.id.tv_iv_contacts_list_item_number);
        }

        @Override
        public void bindCursor(Cursor cursor) {
            nameTextView.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)));
            numberTextView.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.NUMBER)));
            if (TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI))))
                imageView.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI))));
        }
    }
}