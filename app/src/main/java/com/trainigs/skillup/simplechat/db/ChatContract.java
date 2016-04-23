package com.trainigs.skillup.simplechat.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.trainigs.skillup.simplechat.models.Message;

/**
 * Created by Irakli on 4/23/2016
 */
public class ChatContract {

    public static final String DATABASE_NAME = "SimpleChat.db";
    public static final int DATABASE_VERSION = 2;

    public static final String PROVIDER_AUTHORITY = "com.trainigs.skillup.simplechat.provider.SimpleChat";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + PROVIDER_AUTHORITY);

    public static final String PATH_MESSAGES = "messages";
    public static final String PATH_CONVERSATIONS = "conversations";

    public static final class Message implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MESSAGES).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + PROVIDER_AUTHORITY + "/" + PATH_MESSAGES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + PROVIDER_AUTHORITY + "/" + PATH_MESSAGES;

        public static final String TABLE_NAME = "messages";

        public static final String ID = "id";
        public static final String CONTENT = "content";
        public static final String OWNER = "number";
        public static final String DATE = "create_date";
        public static final String TYPE = "type";

        public static Uri buildMessageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static void save(Context context, com.trainigs.skillup.simplechat.models.Message message) {
            ContentValues contentValues = new ContentValues(4);
            contentValues.put(CONTENT, message.getContent());
            contentValues.put(OWNER, message.getOwner());
            contentValues.put(DATE, message.getDate().getTime());
            contentValues.put(TYPE, message.getType());
            context.getContentResolver().insert(CONTENT_URI, contentValues);
        }
    }

    public static final class Conversation implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONVERSATIONS).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + PROVIDER_AUTHORITY + "/" + PATH_CONVERSATIONS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + PROVIDER_AUTHORITY + "/" + PATH_CONVERSATIONS;

        private Conversation() {
        }

        public static final String TABLE_NAME = "Conversations";
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String LAST_MESSAGE = "last_message";
        public static final String LAST_MESSAGE_DATE = "last_message_date";
        public static final String IMAGE_URI = "image_uri";
        public static final String CONTACT_ID = "contact_id";

        public static Uri buildMessageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
