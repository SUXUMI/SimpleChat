package com.trainigs.skillup.simplechat.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.trainigs.skillup.simplechat.db.ConversationsSQLiteHelper;
import com.trainigs.skillup.simplechat.utils.Constants;

/**
 * Created by Irakli on 4/14/2016
 */
public class ConversationContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.PROVIDER_AUTHORITY
            + "/" + Constants.Conversation.TABLE_NAME);

    private static final int CONVERSATIONS = 1;
    private static final int CONVERSATIONS_ID = 2;

    static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(Constants.PROVIDER_AUTHORITY, Constants.Conversation.TABLE_NAME, CONVERSATIONS);
        URI_MATCHER.addURI(Constants.PROVIDER_AUTHORITY, Constants.Conversation.TABLE_NAME + "/#", CONVERSATIONS_ID);
    }

    private ConversationsSQLiteHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new ConversationsSQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long value = database.insert(Constants.Conversation.TABLE_NAME, null, values);
        notifyChange(uri);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result = 0;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case CONVERSATIONS:
                result = sqLiteDatabase.delete(Constants.Conversation.TABLE_NAME, selection, selectionArgs);
                sqLiteDatabase.close();
                break;
            case CONVERSATIONS_ID:
                String id = uri.getPathSegments().get(1);
                result = sqLiteDatabase.delete(Constants.Conversation.TABLE_NAME, Constants.Conversation.ID
                        + "=" + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
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
        return 0;
    }

    private void notifyChange(Uri uri) {
        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
    }
}