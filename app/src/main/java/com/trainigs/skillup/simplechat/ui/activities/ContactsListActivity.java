package com.trainigs.skillup.simplechat.ui.activities;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.trainigs.skillup.simplechat.R;
import com.trainigs.skillup.simplechat.ui.adapters.ContactsAdapter;

import butterknife.Bind;

import butterknife.Bind;

public class ContactsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_PHONE_ID = 1;

    @Bind(R.id.rv_contacts_list)
    RecyclerView contactsRecyclerView;
    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        contactsAdapter = new ContactsAdapter(this);
        contactsRecyclerView.setAdapter(contactsAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_PHONE_ID:
                int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
                if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(args.getString("search", "")));
                    return new CursorLoader(this, uri,
                            new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI},
                            null, null, null);
                }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}