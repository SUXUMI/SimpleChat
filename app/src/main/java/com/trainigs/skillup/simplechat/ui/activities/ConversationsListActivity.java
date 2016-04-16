package com.trainigs.skillup.simplechat.ui.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.trainigs.skillup.simplechat.R;
import com.trainigs.skillup.simplechat.contentproviders.ChatContentProvider;
import com.trainigs.skillup.simplechat.ui.adapters.ConversationsAdapter;
import com.trainigs.skillup.simplechat.utils.Constants;

import butterknife.Bind;

public class ConversationsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.rv_conversation_list)
    RecyclerView conversationRecyclerView;
    ConversationsAdapter conversationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ChatContentProvider.CONVERSATION_CONTENT_URI,
                new String[]{Constants.Conversation.LAST_MESSAGE, Constants.Conversation.TITLE, Constants.Conversation.IMAGE_URI}, null, null, "ASC " + Constants.Conversation.LAST_MESSAGE_DATE);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}