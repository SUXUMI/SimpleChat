package com.trainigs.skillup.simplechat.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.trainigs.skillup.simplechat.R;
import com.trainigs.skillup.simplechat.db.ChatContract;
import com.trainigs.skillup.simplechat.paho.Connection;
import com.trainigs.skillup.simplechat.ui.MyApplication;
import com.trainigs.skillup.simplechat.ui.adapters.ConversationsCursorAdapter;

public class ConversationsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_CONVERSATION_ID = 1;
    private static final int REQUEST_PERMISSIONS_CODE = 100;

    Button numberButton;
    ListView conversationsListView;
    ConversationsCursorAdapter conversationsAdapter;
    Dialog numberDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_list);
        numberButton = (Button) findViewById(R.id.btn_conversation_list_number);
        numberButton.setText("My number: " + MyApplication.getInstance().getNumber());
        conversationsListView = (ListView) findViewById(R.id.lv_conversation_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        conversationsAdapter = new ConversationsCursorAdapter(this, null, false);
        conversationsListView.setAdapter(conversationsAdapter);

        conversationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) conversationsAdapter.getItem(position);
                Intent intent = new Intent(ConversationsListActivity.this, ChatActivity.class);
                intent.putExtra("id", cursor.getInt(cursor.getColumnIndex(ChatContract.Conversation._ID)));
                startActivity(intent);
            }
        });

        conversationsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) conversationsAdapter.getItem(position);
                final int _id = cursor.getInt(cursor.getColumnIndex(ChatContract.Conversation._ID));
                AlertDialog.Builder builder = new AlertDialog.Builder(ConversationsListActivity.this)
                        .setTitle("Do you really want to delete this conversation?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ChatContract.Conversation.deleteConversationById(ConversationsListActivity.this, _id);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.create().show();
                return true;
            }
        });

        getLoaderManager().initLoader(LOADER_CONVERSATION_ID, null, this);
        numberDialog = new Dialog(this);
        numberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        numberDialog.setContentView(R.layout.number_dialog_layout);
        numberDialog.setCancelable(true);
        ((EditText) numberDialog.findViewById(R.id.et_dialog_number_edit)).setText(MyApplication.getInstance().getNumber());
        numberDialog.findViewById(R.id.btn_dialog_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = MyApplication.getInstance().getPrefs().edit();
                editor.putString("number", ((EditText) numberDialog.findViewById(R.id.et_dialog_number_edit)).getText().toString().trim());
                editor.commit();
                Connection.getInstance(MyApplication.getInstance()).subscribe();
                numberDialog.dismiss();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ConversationsListActivity.this, ContactsListActivity.class));
                }
            });
    }

    public void onNumberClick(View view) {
        numberDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = numberDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.READ_CONTACTS};
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE);
        } else {
            Connection.getInstance(MyApplication.getInstance()).connect();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_CONVERSATION_ID:
                return new CursorLoader(this, ChatContract.Conversation.CONTENT_URI,
                        ChatContract.Conversation.PROJECTION, null, null, ChatContract.Conversation.LAST_MESSAGE_DATE + " DESC");
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