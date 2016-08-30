package com.optomiadoctor.model;

/**
 * Created by vikas.patil on 4/25/2016.
 */
public class AppointmentModel {
    String strAppId;
    String strPatientId;
    String strApprovedDate;
    String strApprovedTime;
    String strFirstName;
    String strLastName;
    String strStatus;

    public AppointmentModel(String strAppId, String strPatientId, String strApprovedDate, String strApprovedTime, String strFirstName, String strLastName) {
        this.strAppId = strAppId;
        this.strPatientId = strPatientId;
        this.strApprovedDate = strApprovedDate;
        this.strApprovedTime = strApprovedTime;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
    }


    public AppointmentModel(String strAppId, String strPatientId, String strApprovedDate, String strApprovedTime, String strFirstName, String strLastName, String status) {
        this.strAppId = strAppId;
        this.strPatientId = strPatientId;
        this.strApprovedDate = strApprovedDate;
        this.strApprovedTime = strApprovedTime;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.strStatus = status;
    }

    public String getStrAppId() {
        return strAppId;
    }

    public void setStrAppId(String strAppId) {
        this.strAppId = strAppId;
    }

    public String getStrPatientId() {
        return strPatientId;
    }

    public void setStrPatientId(String strPatientId) {
        this.strPatientId = strPatientId;
    }

    public String getStrApprovedDate() {
        return strApprovedDate;
    }

    public void setStrApprovedDate(String strApprovedDate) {
        this.strApprovedDate = strApprovedDate;
    }

    public String getStrApprovedTime() {
        return strApprovedTime;
    }

    public void setStrApprovedTime(String strApprovedTime) {
        this.strApprovedTime = strApprovedTime;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public void setStrLastName(String strLastName) {
        this.strLastName = strLastName;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }
}
