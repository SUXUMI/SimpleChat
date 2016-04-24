package com.trainigs.skillup.simplechat.ui.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.trainigs.skillup.simplechat.R;
import com.trainigs.skillup.simplechat.db.ChatContract;
import com.trainigs.skillup.simplechat.listeners.DrawableClickListener;
import com.trainigs.skillup.simplechat.models.Conversation;
import com.trainigs.skillup.simplechat.models.Message;
import com.trainigs.skillup.simplechat.paho.Connection;
import com.trainigs.skillup.simplechat.ui.MyApplication;
import com.trainigs.skillup.simplechat.ui.adapters.MessagesCursorAdapter;
import com.trainigs.skillup.simplechat.ui.views.CustomEditText;
import com.trainigs.skillup.simplechat.utils.Utils;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class ChatActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_MESSAGES_ID = 1;
    String number;
    int _id;
    CustomEditText customEditText;
    ListView messagesListView;
    Conversation conversation;
    MessagesCursorAdapter messagesCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle(getIntent().getExtras().getString("name"));
        number = Utils.parseNumber(getIntent().getExtras().getString("number", ""));
        _id = getIntent().getExtras().getInt("id", -1);
        if (_id != -1) {
            conversation = ChatContract.Conversation.getConversationById(this, _id);
            setTitle(conversation.getTitle());
            getLoaderManager().initLoader(LOADER_MESSAGES_ID, null, this);
        }
        messagesListView = (ListView) findViewById(R.id.lv_chat_list);
        messagesCursorAdapter = new MessagesCursorAdapter(this, null, false);
        messagesListView.setAdapter(messagesCursorAdapter);
        customEditText = (CustomEditText) findViewById(R.id.et_chat_send_message);
        customEditText.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                String sendText = customEditText.getText().toString().trim();
                if (TextUtils.isEmpty(sendText))
                    return;
                if (_id == -1) {
                    Conversation conversation = new Conversation();
                    conversation.setTitle(ChatActivity.this.getTitle().toString());
                    conversation.setLastMessage(sendText);
                    conversation.setLastMessageDate(new Date());
                    conversation.setOwner(number);
                    conversation.setImageUri(ChatActivity.this.getIntent().getExtras().getString("image", ""));
                    _id = ChatContract.Conversation.save(ChatActivity.this, conversation);
                    if (_id != -1) {
                        ChatActivity.this.conversation = conversation;
                        getLoaderManager().initLoader(LOADER_MESSAGES_ID, null, ChatActivity.this);
                    }
                }
                if (_id != -1) {
                    Message message = new Message();
                    message.setOwner(MyApplication.getInstance().getNumber());
                    message.setContent(sendText);
                    message.setConversationId(_id);
                    message.setType(1);
                    ChatContract.Message.save(ChatActivity.this, message);
                    sendMessage(message);

                    conversation.setLastMessageDate(new Date());
                    conversation.setLastMessage(customEditText.getText().toString().trim());
                    ChatContract.Conversation.update(ChatActivity.this, conversation);
                }
                customEditText.setText("");
            }
        });
    }

    private void sendMessage(Message message) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("number", MyApplication.getInstance().getNumber());
            jsonObject.put("content", message.getContent());
            Connection.getInstance(this).sendMessage(conversation.getOwner(), jsonObject.toString(), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.i("SimpleChat", "Message Send success");
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.e("SimpleChat", "Message Send Fail");
                }
            });
        } catch (JSONException ignore) {
        }
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_MESSAGES_ID:
                return new CursorLoader(this, ChatContract.Message.CONTENT_URI,
                        new String[]{ChatContract.Message.CONTENT, ChatContract.Message.TYPE, ChatContract.Message.DATE, ChatContract.Message.OWNER, ChatContract.Message._ID},
                        ChatContract.Message.CONVERSATION_ID + " = ?", new String[]{_id + ""}, ChatContract.Message.DATE + " ASC");
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