package com.trainigs.skillup.simplechat.ui.activities;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.trainigs.skillup.simplechat.R;
import com.trainigs.skillup.simplechat.paho.Connection;
import com.trainigs.skillup.simplechat.ui.MyApplication;
import com.trainigs.skillup.simplechat.ui.adapters.ConversationsAdapter;

public class ConversationsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_CONVERSATION_ID = 1;
    private static final int REQUEST_PERMISSIONS_CODE = 100;

    RecyclerView conversationRecyclerView;
    ConversationsAdapter conversationsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_list);
        conversationRecyclerView = (RecyclerView) findViewById(R.id.rv_conversation_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        conversationsAdapter = new ConversationsAdapter(this);
        conversationRecyclerView.setAdapter(conversationsAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ConversationsListActivity.this, ContactsListActivity.class));
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int hasReadSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED
                && hasReadSmsPermission != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS};
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE);
        } else {
            Connection.getInstance(MyApplication.getInstance()).connect();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_CONVERSATION_ID:
//                return new CursorLoader(this, ChatContentProvider.CONVERSATION_CONTENT_URI,
//                        new String[]{Constants.Conversation.LAST_MESSAGE, Constants.Conversation.TITLE, Constants.Conversation.IMAGE_URI}, null, null, "ASC " + Constants.Conversation.LAST_MESSAGE_DATE);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_CONVERSATION_ID:
                conversationsAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_CONVERSATION_ID:
                conversationsAdapter.swapCursor(null);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CODE:
                if (permissions.length > 0 && Manifest.permission.READ_CONTACTS.equals(permissions[0]))
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                        finish();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}