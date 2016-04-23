package com.trainigs.skillup.simplechat.ui.activities;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.trainigs.skillup.simplechat.R;
import com.trainigs.skillup.simplechat.ui.adapters.ContactsCursorAdapter;

public class ContactsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_PHONE_ID = 1;

    ListView contactsListView;
    ContactsCursorAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        contactsListView = (ListView) findViewById(R.id.lv_contacts_list);
//        contactsAdapter = new ContactsAdapter(this);
//        contactsRecyclerView.setAdapter(contactsAdapter);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) contactsAdapter.getItem(position);
                Intent intent = new Intent(ContactsListActivity.this, ChatActivity.class);
                intent.putExtra("number", cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                intent.putExtra("name", cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                startActivity(intent);
                finish();
            }
        });
        contactsAdapter = new ContactsCursorAdapter(this, null, 0);
        contactsListView.setAdapter(contactsAdapter);
        getLoaderManager().initLoader(LOADER_PHONE_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_PHONE_ID:
                int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
                if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                    return new CursorLoader(this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI, ContactsContract.CommonDataKinds.Phone._ID},
                            ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = 1",
                            null, null);
                }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_PHONE_ID:
                contactsAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}