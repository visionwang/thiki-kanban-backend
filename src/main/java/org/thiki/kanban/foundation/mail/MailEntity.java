package org.thiki.kanban.foundation.mail;

import org.thiki.kanban.notification.Notification;
import org.thiki.kanban.notification.NotificationType;

/**
 * Created by xubt on 8/14/16.
 */
public class MailEntity {
    private String userName;
    private String receiver;
    private String sender;
    private String subject;

    private String dateline;
    private String templateName;
    private String senderUserName;
    private String receiverUserName;

    private NotificationType notificationType;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getReceiverUserName() {
        return receiverUserName;
    }

    public void setReceiverUserName(String receiverUserName) {
        this.receiverUserName = receiverUserName;
    }

    public Notification newNotification() {
        Notification notification = new Notification();
        notification.setTitle(this.getSubject());
        notification.setReceiver(this.receiverUserName);
        notification.setSender(this.senderUserName);
        notification.setContent("");
        notification.setType(notificationType.type());
        return notification;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
