package com.trainigs.skillup.simplechat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.trainigs.skillup.simplechat.utils.Constants;

/**
 * Created by Irakli on 4/14/2016
 */
public class ChatSQLiteHelper extends SQLiteOpenHelper {

    private static final String CONVERSATION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + ChatContract.Conversation.TABLE_NAME
            + "(" + ChatContract.Conversation.ID + " INTEGER PRIMARY KEY, " + ChatContract.Conversation.TITLE + " TEXT);";

    private static final String MESSAGE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + ChatContract.Message.TABLE_NAME
            + "(" + ChatContract.Message.ID + " INTEGER PRIMARY KEY, " + ChatContract.Message.OWNER + " TEXT, "
            + ChatContract.Message.TYPE + " INTEGER, " + ChatContract.Message.DATE + " INTEGER, " + ChatContract.Message.CONTENT + " TEXT);";

    public ChatSQLiteHelper(Context context) {
        super(context, ChatContract.DATABASE_NAME, null, ChatContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CONVERSATION_TABLE_CREATE);
        db.execSQL(MESSAGE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ChatContract.Conversation.TABLE_NAME);
        onCreate(db);
    }
}