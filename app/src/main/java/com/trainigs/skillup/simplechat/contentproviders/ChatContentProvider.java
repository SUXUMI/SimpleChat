package com.trainigs.skillup.simplechat.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.trainigs.skillup.simplechat.db.ChatContract;
import com.trainigs.skillup.simplechat.db.ChatSQLiteHelper;
import com.trainigs.skillup.simplechat.utils.Constants;

/**
 * Created by Irakli on 4/14/2016
 */
public class ChatContentProvider extends ContentProvider {

    private static final int CONVERSATIONS = 1;
    private static final int CONVERSATIONS_ID = 2;
    private static final int MESSAGES = 3;

    static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(ChatContract.PROVIDER_AUTHORITY, ChatContract.PATH_CONVERSATIONS, CONVERSATIONS);
        URI_MATCHER.addURI(ChatContract.PROVIDER_AUTHORITY, ChatContract.PATH_MESSAGES, MESSAGES);
    }

    private ChatSQLiteHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new ChatSQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor returnCursor;
        switch (URI_MATCHER.match(uri)) {
            case MESSAGES:
                returnCursor = database.query(ChatContract.Message.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CONVERSATIONS:
                returnCursor = database.query(ChatContract.Conversation.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case MESSAGES:
                return ChatContract.Message.CONTENT_TYPE;
            case CONVERSATIONS:
                return ChatContract.Conversation.CONTENT_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Uri returnUri;
        switch (URI_MATCHER.match(uri)) {
            case MESSAGES:
                long id = database.insert(ChatContract.Message.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = ChatContract.Message.buildMessageUri(id);
                else throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case CONVERSATIONS:
                id = database.insert(ChatContract.Conversation.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = ChatContract.Message.buildMessageUri(id);
                else throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        notifyChange(uri);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result = 0;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case CONVERSATIONS:
                result = sqLiteDatabase.delete(ChatContract.Conversation.TABLE_NAME, selection, selectionArgs);
                break;
            case MESSAGES:
                result = sqLiteDatabase.delete(ChatContract.Message.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                result = -1;
        }
        sqLiteDatabase.close();
        if (result == -1)
            throw new IllegalArgumentException("Unknown URI " + uri);
        notifyChange(uri);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (URI_MATCHER.match(uri)) {
            case CONVERSATIONS:
                rowsUpdated = database.update(ChatContract.Conversation.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MESSAGES:
                rowsUpdated = database.update(ChatContract.Message.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0)
            notifyChange(uri);
        return rowsUpdated;
    }

    private void notifyChange(Uri uri) {
        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
    }
}