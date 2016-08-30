package com.optomiadoctor.model;

/**
 * Created by vikas.patil on 3/25/2016.
 */
public class SpecialityType {
    String strID;
    String strCategoryName;
String stravailableCount;
    public SpecialityType(String strID, String strCategoryName) {
        this.strID = strID;
        this.strCategoryName = strCategoryName;
    }

    public SpecialityType(String strID, String strCategoryName, String stravailableCount) {
        this.strID = strID;
        this.strCategoryName = strCategoryName;
        this.stravailableCount = stravailableCount;
    }

    public String getStrID() {
        return strID;
    }

    public void setStrID(String strID) {
        this.strID = strID;
    }

    public String getStrCategoryName() {
        return strCategoryName;
    }

    public void setStrCategoryName(String strCategoryName) {
        this.strCategoryName = strCategoryName;
    }

    public String getStravailableCount() {
        return stravailableCount;
    }

    public void setStravailableCount(String stravailableCount) {
        this.stravailableCount = stravailableCount;
    }
}
