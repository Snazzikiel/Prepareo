package com.uow.snazzikiel.prepareo;

import java.util.ArrayList;

public class owlData {

    private String userName;
    private String date;
    private String mapKey;
    private Long mapTime;

    public static ArrayList<owlData> owlInfo;

    public owlData(String userName, String date, String mapKey, Long mapTime){
        this.userName = userName;
        this.date = date;
        this.mapKey = mapKey;
        this.mapTime = mapTime;
    }

    //get
    public String getUserName(){
        return this.userName;
    }

    public String getDate() { return this.date; }

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

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void mapTime(Long mapTime){
        this.mapTime = mapTime;
    }


}
