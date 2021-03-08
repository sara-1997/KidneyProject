package com.example.kidneyproject.Class;

public class Notification {
    String TitleNotification , MessageNotification , TypeNotification , NameReceiver ,Key;
    String SenderNotification , ReceiverNotification  ;

    public String getKey() {
        return Key;
    }

    public Notification(String titleNotification, String messageNotification, String typeNotification, String nameReceiver, String key, String senderNotification, String receiverNotification) {
        TitleNotification = titleNotification;
        MessageNotification = messageNotification;
        TypeNotification = typeNotification;
        NameReceiver = nameReceiver;
        Key = key;
        SenderNotification = senderNotification;
        ReceiverNotification = receiverNotification;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Notification() {
    }



    public String getNameReceiver() {
        return NameReceiver;
    }

    public void setNameReceiver(String nameReceiver) {
        NameReceiver = nameReceiver;
    }

    public String getTitleNotification() {
        return TitleNotification;
    }

    public void setTitleNotification(String titleNotification) {
        TitleNotification = titleNotification;
    }

    public String getMessageNotification() {
        return MessageNotification;
    }

    public void setMessageNotification(String messageNotification) {
        MessageNotification = messageNotification;
    }

    public String getTypeNotification() {
        return TypeNotification;
    }

    public void setTypeNotification(String typeNotification) {
        TypeNotification = typeNotification;
    }

    public String getSenderNotification() {
        return SenderNotification;
    }

    public void setSenderNotification(String senderNotification) {
        SenderNotification = senderNotification;
    }

    public String getReceiverNotification() {
        return ReceiverNotification;
    }

    public void setReceiverNotification(String receiverNotification) {
        ReceiverNotification = receiverNotification;
    }
}
