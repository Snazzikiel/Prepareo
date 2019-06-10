package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		Alec
 * Assisted:		David
 ***********************************************/

import java.util.ArrayList;
import java.util.HashMap;

/**
    Class:   owlData
    ---------------------------------------
    Used to store dates and activities taken from the OWL file
*/
public class owlData {

    private String userName;
    private String date;
    private String startTime;
    private String endTime;
    private String mapKey;
    private Long mapTime;

    public static ArrayList<owlData> owlInfo;
    public static HashMap<String, ArrayList<String>> activityList;

    public owlData(String userName, String date, String startTime, String endTime, String mapKey, Long mapTime){
        this.userName = userName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.mapKey = mapKey;
        this.mapTime = mapTime;
    }

    //get
    public String getUserName(){
        return this.userName;
    }

    public String getDate() { return this.date; }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getMapKey(){
        return this.mapKey;
    }

    public Long getMapTime(){
        return this.mapTime;
    }

    //set
    public void setMapKey(String mapKey){
        this.mapKey = mapKey;
    }

    public void setDate(String date) { this.date = date; }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void mapTime(Long mapTime){
        this.mapTime = mapTime;
    }


}
