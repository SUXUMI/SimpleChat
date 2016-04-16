package com.trainigs.skillup.simplechat.utils;

/**
 * Created by Irakli on 4/14/2016
 */
public final class Constants {

    private Constants() {
    }

    public static final String PROVIDER_AUTHORITY = "com.trainigs.skillup.simplechat.provider.SimpleChat";

    public final class DB {
        public DB() {
        }

        public static final String DATABASE_NAME = "SimpleChat.db";
        public static final int DATABASE_VERSION = 1;
    }

    public final class Conversation {
        private Conversation() {
        }

        public static final String TABLE_NAME = "Conversations";
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String LAST_MESSAGE = "last_message";
        public static final String LAST_MESSAGE_DATE = "last_message_date";
        public static final String IMAGE_URI = "image_uri";
        public static final String CONTACT_ID = "contact_id";
    }

    public final class Message {
        private Message() {
        }

        public static final String TABLE_NAME = "Messages";
        public static final String ID = "id";
        public static final String CONTENT = "content";

    }
}