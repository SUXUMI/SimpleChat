package com.trainigs.skillup.simplechat.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

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
        public static final String OWNER = "owner";
        public static final String DATE = "date";
        public static final String TYPE = "type";

        public static Uri buildMessageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
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
