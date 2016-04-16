package com.trainigs.skillup.simplechat.models;

import android.net.Uri;
import android.text.TextUtils;

import java.util.Date;

/**
 * Created by Irakli on 4/5/2016
 */
public class Conversation {

    private int id;

    private String title;

    private String lastMessage;

    private Date lastMessageDate;

    private Uri imageUri;

    private int contactId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(long lastMessageDate) {
        this.lastMessageDate = new Date(lastMessageDate);
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        if (!TextUtils.isEmpty(imageUri))
            this.imageUri = Uri.parse(imageUri);
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}