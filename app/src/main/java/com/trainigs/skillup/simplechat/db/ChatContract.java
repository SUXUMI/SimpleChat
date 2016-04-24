package com.trainigs.skillup.simplechat.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.trainigs.skillup.simplechat.models.Conversation;
import com.trainigs.skillup.simplechat.models.Message;
import com.trainigs.skillup.simplechat.utils.Utils;

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

        public static final String CONTENT = "content";
        public static final String OWNER = "number";
        public static final String DATE = "create_date";
        public static final String TYPE = "type";
        public static final String CONVERSATION_ID = "conversation_id";

        public static Uri buildMessageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static void save(Context context, com.trainigs.skillup.simplechat.models.Message message) {
            ContentValues contentValues = new ContentValues(5);
            contentValues.put(CONTENT, message.getContent());
            contentValues.put(OWNER, message.getOwner());
            contentValues.put(DATE, message.getDate().getTime());
            contentValues.put(TYPE, message.getType());
            contentValues.put(CONVERSATION_ID, message.getConversationId());
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
        public static final String TITLE = "title";
        public static final String LAST_MESSAGE = "last_message";
        public static final String LAST_MESSAGE_DATE = "last_message_date";
        public static final String IMAGE_URI = "image_uri";
        public static final String OWNER = "owner";
        public static final String CONTACT_ID = "contact_id";

        public static final String[] PROJECTION = {ChatContract.Conversation.LAST_MESSAGE, Conversation.OWNER, ChatContract.Conversation.TITLE, ChatContract.Conversation.IMAGE_URI,
                ChatContract.Conversation.LAST_MESSAGE_DATE, ChatContract.Conversation._ID};

        public static Uri buildMessageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static int save(Context context, com.trainigs.skillup.simplechat.models.Conversation conversation) {
            ContentValues contentValues = new ContentValues(5);
            contentValues.put(TITLE, conversation.getTitle());
            contentValues.put(LAST_MESSAGE, conversation.getLastMessage());
            contentValues.put(OWNER, conversation.getOwner());
            contentValues.put(LAST_MESSAGE_DATE, conversation.getLastMessageDate().getTime());
            if (conversation.getImageUri() != null)
                contentValues.put(IMAGE_URI, conversation.getImageUri().toString());
            else contentValues.put(IMAGE_URI, "");
            Uri uri = context.getContentResolver().insert(CONTENT_URI, contentValues);
            if (uri != null)
                return Integer.parseInt(Utils.getLastBitFromUrl(uri.toString()));
            else return -1;
        }

        public static int update(Context context, com.trainigs.skillup.simplechat.models.Conversation conversation) {
            ContentValues contentValues = new ContentValues(4);
            contentValues.put(TITLE, conversation.getTitle());
            contentValues.put(LAST_MESSAGE, conversation.getLastMessage());
            contentValues.put(LAST_MESSAGE_DATE, conversation.getLastMessageDate().getTime());
            if (conversation.getImageUri() != null)
                contentValues.put(IMAGE_URI, conversation.getImageUri().toString());
            else contentValues.put(IMAGE_URI, "");
            return context.getContentResolver().update(CONTENT_URI, contentValues, _ID + " = ?", new String[]{conversation.getId() + ""});
        }

        public static void deleteConversationById(Context context, int id) {
            context.getContentResolver().delete(Message.CONTENT_URI, Message.CONVERSATION_ID + " = ?", new String[]{id + ""});
            context.getContentResolver().delete(CONTENT_URI, _ID + " = ?", new String[]{id + ""});
        }

        public static com.trainigs.skillup.simplechat.models.Conversation getConversationById(Context context, int id) {
            com.trainigs.skillup.simplechat.models.Conversation conversation = null;
            Cursor cursor = context.getContentResolver().query(CONTENT_URI, PROJECTION,
                    _ID + " = ?", new String[]{id + ""}, ChatContract.Conversation.LAST_MESSAGE_DATE + " ASC");
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    conversation = getConversationFromCursor(cursor);
                }
                cursor.close();
            }
            return conversation;
        }

        public static com.trainigs.skillup.simplechat.models.Conversation getConversationByNumber(Context context, String number) {
            com.trainigs.skillup.simplechat.models.Conversation conversation = null;
            Cursor cursor = context.getContentResolver().query(CONTENT_URI, PROJECTION,
                    OWNER + " = ?", new String[]{number + ""}, ChatContract.Conversation.LAST_MESSAGE_DATE + " ASC");
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    conversation = getConversationFromCursor(cursor);
                }
                cursor.close();
            }
            return conversation;
        }

        private static com.trainigs.skillup.simplechat.models.Conversation getConversationFromCursor(Cursor cursor) {
            com.trainigs.skillup.simplechat.models.Conversation conversation = new com.trainigs.skillup.simplechat.models.Conversation();
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                conversation.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
                conversation.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                conversation.setLastMessage(cursor.getString(cursor.getColumnIndex(LAST_MESSAGE)));
                conversation.setLastMessageDate(cursor.getInt(cursor.getColumnIndex(LAST_MESSAGE_DATE)));
                conversation.setImageUri(cursor.getString(cursor.getColumnIndex(IMAGE_URI)));
                conversation.setOwner(cursor.getString(cursor.getColumnIndex(OWNER)));
            }
            return conversation;
        }
    }
}
