package com.trainigs.skillup.simplechat.ui.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.trainigs.skillup.simplechat.R;
import com.trainigs.skillup.simplechat.db.ChatContract;
import com.trainigs.skillup.simplechat.listeners.DrawableClickListener;
import com.trainigs.skillup.simplechat.models.Message;
import com.trainigs.skillup.simplechat.ui.MyApplication;
import com.trainigs.skillup.simplechat.ui.adapters.MessagesCursorAdapter;
import com.trainigs.skillup.simplechat.ui.views.CustomEditText;
import com.trainigs.skillup.simplechat.utils.Utils;

public class ChatActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_MESSAGES_ID = 1;
    String number;
    CustomEditText customEditText;
    ListView messagesListView;
    MessagesCursorAdapter messagesCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle(getIntent().getExtras().getString("name"));
        number = Utils.parseNumber(getIntent().getExtras().getString("number", ""));
        messagesListView = (ListView) findViewById(R.id.lv_chat_list);
        messagesCursorAdapter = new MessagesCursorAdapter(this, null, false);
        messagesListView.setAdapter(messagesCursorAdapter);
        getLoaderManager().initLoader(LOADER_MESSAGES_ID, null, this);
        customEditText = (CustomEditText) findViewById(R.id.et_chat_send_message);
        customEditText.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                Message message = new Message();
                message.setOwner(MyApplication.getInstance().getPhoneNumber());
                message.setContent(customEditText.getText().toString().trim());
                message.setType(1);
                ChatContract.Message.save(ChatActivity.this, message);
                customEditText.setText("");
            }
        });
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_MESSAGES_ID:
                return new CursorLoader(this, ChatContract.Message.CONTENT_URI,
                        new String[]{ChatContract.Message.CONTENT, ChatContract.Message.TYPE, ChatContract.Message.DATE, ChatContract.Message.OWNER},
                        ChatContract.Message.OWNER + " = ?", new String[]{number}, "ASC " + ChatContract.Message.DATE);
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_MESSAGES_ID:
                messagesCursorAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_MESSAGES_ID:
                messagesCursorAdapter.swapCursor(null);
                break;
        }
    }
}