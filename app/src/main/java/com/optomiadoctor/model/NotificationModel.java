package com.optomiadoctor.model;

/**
 * Created by sandeep.magar on 6/1/2016.
 */
public class NotificationModel {
    private String notification;
    private String notify_date;
    private String notify_time;

    public NotificationModel() {
    }

    public NotificationModel(String notification, String notify_date, String notify_time) {
        this.notification = notification;
        this.notify_date = notify_date;
        this.notify_time = notify_time;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getNotify_date() {
        return notify_date;
    }

    public void setNotify_date(String notify_date) {
        this.notify_date = notify_date;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "notification='" + notification + '\'' +
                ", notify_date='" + notify_date + '\'' +
                ", notify_time='" + notify_time + '\'' +
                '}';
    }
}
