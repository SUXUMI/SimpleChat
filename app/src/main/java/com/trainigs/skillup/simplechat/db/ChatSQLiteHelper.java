package com.trainigs.skillup.simplechat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.trainigs.skillup.simplechat.utils.Constants;

/**
 * Created by Irakli on 4/14/2016
 */
public class ChatSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "intelphone.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CONVERSATION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + Constants.Conversation.TABLE_NAME
            + "(" + Constants.Conversation.ID + " INTEGER PRIMARY KEY, " + Constants.Conversation.TITLE + " TEXT);";

    private static final String MESSAGE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + Constants.Message.TABLE_NAME
            + "(" + Constants.Message.ID + " INTEGER PRIMARY KEY, " + Constants.Message.CONTENT + " TEXT);";

    public ChatSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CONVERSATION_TABLE_CREATE);
        db.execSQL(MESSAGE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.Conversation.TABLE_NAME);
        onCreate(db);
    }
}