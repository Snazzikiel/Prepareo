package com.uow.snazzikiel.prepareo;

import java.util.ArrayList;

public class owlData {

    private String userName;
    private String mapKey;
    private Long mapTime;

    public static ArrayList<owlData> owlInfo;

    public owlData(String userName, String mapKey, Long mapTime){
        this.userName = userName;
        this.mapKey = mapKey;
        this.mapTime = mapTime;
    }

    //get
    public String getUserName(){
        return this.userName;
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

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void mapTime(Long mapTime){
        this.mapTime = mapTime;
    }


}
