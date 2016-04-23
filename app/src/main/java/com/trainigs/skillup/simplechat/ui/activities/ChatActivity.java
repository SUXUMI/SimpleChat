package com.trainigs.skillup.simplechat.ui.activities;

import android.app.LoaderManager;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trainigs.skillup.simplechat.R;
import com.trainigs.skillup.simplechat.utils.Utils;

public class ChatActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_MESSAGES_ID = 1;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle(getIntent().getExtras().getString("name"));
        number = Utils.parseNumber(getIntent().getExtras().getString("number", ""));
        getLoaderManager().initLoader(LOADER_MESSAGES_ID, null, this);

    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }
}