package com.zybooks.universityconnect;


public class Message {
    private String messageText;
    private String messageUser;
    private String Uid;
    private long messageTime;

    public Message(String messageText, String messageUser, String Uid, long messageTime) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.Uid = Uid;
        this.messageTime = messageTime;
    }

    public Message() {

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
